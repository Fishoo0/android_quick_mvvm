package com.common.quick.bindingadapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.common.quick.BR;
import com.common.quick.mvvm.QuickBaseActivity;
import com.common.quick.mvvm.QuickBaseFragment;
import com.common.quick.utils.QuickUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * RecyclerView 的 databinding adapter
 */
public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.BindingViewHolder> {

    protected String TAG = RecycleViewAdapter.class.getSimpleName();

    private int selectedItemPosition = Integer.MAX_VALUE;

    protected Object mObject;
    protected Object mView;
    protected Object mViewModel;
    int mMaxLines = 0;
    private List<Object> mItems = new ArrayList<>();
    private ItemBinding mItemBinding;
    private final RecyclerView mRecycleView;

    /**
     * Selector of recycle view.
     * <p>
     * This class enables selector effect for recycle view.
     */
    public static class BindingAdapterSelector {

        public static final String TAG = BindingAdapterSelector.class.getSimpleName();

        private final RecyclerView.Adapter mAdapter;

        // the selector status views
        private boolean mDirtyFlag = true;
        private int mSelectPosition = -1;
        private View mSelectView;

        // the recycleview in recyceview status view
        private RecyclerView mRecycleView = null;
        private RecyclerView mRIR = null;

        // the out set
        int mInitSelectPosition = -2;
        boolean mEnableSelector = false;

        /**
         * On item click listener
         */
        OnItemClickListener mOnItemClickListener;


        public BindingAdapterSelector(RecyclerView.Adapter adapter) {
            mAdapter = adapter;
        }

        /**
         * Enable selector or not.
         *
         * @param view
         * @return
         */
        protected boolean enableSelector(View view) {
            return mEnableSelector;
        }


        /**
         * On item clicked
         *
         * @param v
         * @param position
         * @param object
         */
        public void onItemClick(View v, int position, Object object) {
            Log.v(TAG, "onItemClick position -> " + position + ", object -> " + object + ", mSelectPosition -> " + mSelectPosition);
            initRIR(v);

            if (enableSelector(v)) {
                resetRIR(v);
                if (position != mSelectPosition || mDirtyFlag) {
                    v.setSelected(true);
                    if (mSelectView != null) {
                        mSelectView.setSelected(false);
                    }
                    mSelectPosition = position;
                    mSelectView = v;
                    mDirtyFlag = false;
                    markRIR();
                }
            }
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(position, object);
            }
        }

        /**
         * Called when update
         *
         * @param view
         * @param position
         * @param object
         */
        public void onItemUpdate(View view, int position, Object object) {
            if (mDirtyFlag && position == mSelectPosition) {
                onItemClick(view, position, object);
            } else if (mInitSelectPosition >= -1) { // we are trying to select one object
                if (mSelectPosition < 0) { // select first
                    if (mInitSelectPosition == -1) {
                        if (view.isEnabled()) {
                            onItemClick(view, position, object);
                        }
                    } else if (position == mInitSelectPosition) {
                        if (!view.isEnabled()) {
                            throw new RuntimeException("The init position is not enabled.");
                        }
                        onItemClick(view, position, object);
                    }
                }
            }

            if (enableSelector(view)) {
                if (position != mSelectPosition) {
                    if (view == mSelectView) {
                        view.setSelected(false);
                        mSelectView = null;
                    }
                } else {
                    view.setSelected(true);
                    mSelectView = view;
                }
            }
        }


        /**
         * Reset the selector status.
         */
        public void reset() {
            Log.v(TAG, "rest");
            if (mSelectView != null) {
                mSelectView.setSelected(false);
            }
            mSelectView = null;
            mSelectPosition = -1;
            mDirtyFlag = true;
        }

        public void notifyDataSetChanged(boolean resetSelected) {
//        LogUtils.v(TAG, "notifyDataSetChanged -> " + resetSelected,true);
            mDirtyFlag = true;
            if (resetSelected || mSelectPosition > mAdapter.getItemCount() - 1) {
                reset();
            }
        }

        protected final RecyclerView findParentRecycleView(View view) {
            ViewParent parent = view instanceof ViewParent ? (ViewParent) view : view.getParent();
            while (parent != null) {
                if (parent instanceof RecyclerView) {
                    return (RecyclerView) parent;
                }
                parent = parent.getParent();
            }
            return null;
        }

        protected void initRIR(View view) {
            if (mRecycleView == null) {
                RecyclerView recyclerView = findParentRecycleView(view);
                if (recyclerView == null) {
                    // we have not attached to any view yet?
                    view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                        @Override
                        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                            initRIR(view);

                            // remove this staff
                            view.removeOnLayoutChangeListener(this);
                        }
                    });
                    return;
                }
                mRecycleView = recyclerView;
                mRIR = findParentRecycleView((View) mRecycleView.getParent());
                // we have last
                if (mSelectPosition >= 0) {
                    markRIR();
                }
            }
        }

        protected void resetRIR(View view) {
            if (mRIR != null) {
                // reset last state.
                if (mRIR.getTag() instanceof BindingAdapterSelector && mRIR.getTag() != this) {
                    ((BindingAdapterSelector) mRIR.getTag()).reset();
                }
            }
        }

        protected void markRIR() {
            if (mRIR != null) {
                mRIR.setTag(this);
            }
        }
    }

    public interface ItemBinding {

        int getItemViewType(Object item);

        ViewDataBinding createBinding(@NonNull ViewGroup parent, int viewType);

        void bindView(ViewDataBinding binding, Object item, int position);
    }

    public static class BaseItemBinding implements ItemBinding {

        public Context context;
        private LayoutInflater mInflater;
        private int mItemLayout;
        private RecyclerView.Adapter mAdapter;

        public BaseItemBinding() {
        }

        public BaseItemBinding(Context context) {
            this.context = context;
            mInflater = LayoutInflater.from(context);
        }

        public BaseItemBinding(int itemLayout) {
            mItemLayout = itemLayout;
        }

        public BaseItemBinding(Context context, int itemLayout) {
            this(context);
            mItemLayout = itemLayout;
        }

        @Override
        public int getItemViewType(Object item) {
            return mItemLayout;
        }

        @Override
        public ViewDataBinding createBinding(@NonNull ViewGroup parent, int viewType) {
            Log.v(getAdapter().TAG, "createBinding");
            if (mInflater == null)
                mInflater = LayoutInflater.from(parent.getContext());
            ViewDataBinding binding = DataBindingUtil.inflate(mInflater, viewType, parent, false);
            return binding;
        }

        @Override
        @CallSuper
        public void bindView(ViewDataBinding binding, Object item, int position) {
            binding.setVariable(BR.item, item);
            binding.setVariable(BR.position, position);
            binding.setVariable(BR.adapter, getAdapter());
            binding.setVariable(BR.object, getAdapter().mObject);
            if (getAdapter().mView == null) {
                // fix recycle view in recycle view mView
                getAdapter().tryFindFragmentOrActivity();
            }
            if (getAdapter().mView != null) {
                binding.setVariable(BR.view, getAdapter().mView);
                if (getAdapter().mView instanceof QuickBaseActivity) {
                    binding.setVariable(BR.activity, getAdapter().mView);
                    binding.setVariable(BR.viewModel, ((QuickBaseActivity) getAdapter().mView).getViewModel());
                    binding.setLifecycleOwner((LifecycleOwner) getAdapter().mView);
                } else if (getAdapter().mView instanceof QuickBaseFragment) {
                    binding.setVariable(BR.activity, ((QuickBaseFragment) getAdapter().mView).getActivity());
                    binding.setVariable(BR.fragment, getAdapter().mView);
                    binding.setLifecycleOwner((LifecycleOwner) getAdapter().mView);
                    if (((QuickBaseFragment) getAdapter().mView).getParentFragment() instanceof DialogFragment) {
                        binding.setVariable(BR.dialog, ((QuickBaseFragment) getAdapter().mView).getParentFragment());
                    }
                    binding.setVariable(BR.viewModel, ((QuickBaseFragment) getAdapter().mView).getViewModel());
                }
            } else {
                Log.e(getAdapter().TAG, "ERROR!!! getAdapter().mView == null, this is an error state !!!");
            }
        }

        protected RecycleViewAdapter getAdapter() {
            return (RecycleViewAdapter) mAdapter;
        }

    }

    public void setItemBinding(ItemBinding binding) {
        mItemBinding = binding;
        if (binding instanceof BaseItemBinding)
            ((BaseItemBinding) mItemBinding).mAdapter = this;
    }

    /**
     * Support multiple viewType.
     */
    public static class BaseMultipleTypeItemBinding extends BaseItemBinding {

        final Map<Class, Integer> mLayouts;

        public BaseMultipleTypeItemBinding(Context context, Map<Class, Integer> layouts) {
            super(context);
            mLayouts = layouts;
            if (layouts == null) {
                throw new IllegalArgumentException("layouts can not be null !");
            }
        }

        @Override
        public int getItemViewType(Object item) {
            int layout = mLayouts.get(item.getClass());
            if (layout <= 0) {
                throw new RuntimeException("Found unsupported of item -> " + item.getClass());
            }
            return layout;
        }

        @Override
        public ViewDataBinding createBinding(@NonNull ViewGroup parent, int viewType) {
            return super.createBinding(parent, viewType);
        }
    }

    /**
     * See {@link BindingAdapterSelector} for detail.
     */
    protected BindingAdapterSelector mItemSelectorTool;


    public RecycleViewAdapter(RecyclerView recyclerView) {
        mItemSelectorTool = new BindingAdapterSelector(this);
        mRecycleView = recyclerView;
        tryFindFragmentOrActivity();
    }

    public void tryFindFragmentOrActivity() {
        if (mView == null) {
            if (QuickUtils.isViewAddedToParent(mRecycleView)) {
                mView = QuickUtils.tryFindFragmentOrActivity(mRecycleView);
            }
        }
    }

    /**
     * Internal adapter
     */
    public static class BindingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ViewDataBinding mBinding;
        private int mPosition;
        private Object mItem;

        private BindingAdapterSelector mItemSelector;

        public BindingViewHolder(@NonNull ViewDataBinding binding, BindingAdapterSelector itemSelector) {
            super(binding.getRoot());
            if (binding.getRoot().isEnabled()) {
                binding.getRoot().setOnClickListener(this);
            }
            mBinding = binding;
            mItemSelector = itemSelector;
        }

        @Override
        public void onClick(View v) {
            mItemSelector.onItemClick(v, mPosition, mItem);
        }

        public void bind(ItemBinding binding, Object item, int position) {
            mPosition = position;
            mItem = item;
            binding.bindView(mBinding, item, position);
            mBinding.executePendingBindings();
            mItemSelector.onItemUpdate(mBinding.getRoot(), position, mItem);
        }
    }

    @NonNull
    @Override
    public BindingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Log.v(TAG,"onCreateViewHolder -> " + viewType);
        ViewDataBinding binding = mItemBinding.createBinding(parent, viewType);
        return new BindingViewHolder(binding, mItemSelectorTool);
    }

    @Override
    public void onBindViewHolder(@NonNull BindingViewHolder holder, int position) {
        //Log.v(TAG,"onBindViewHolder -> " + position);
        Object item = mItems.get(position);
        holder.bind(mItemBinding, item, position);
    }

    @Override
    public int getItemCount() {
        if (mMaxLines > 0) {
            return Math.min(mMaxLines, mItems.size());
        }
        return mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mItemBinding.getItemViewType(mItems.get(position));
    }

    public List<Object> getData() {
        return mItems;
    }

    public void addAll(List<? extends Object> items) {
        if (items == null || items.isEmpty()) return;
        int size = mItems.size();
        mItems.addAll(items);
        notifyItemRangeInserted(size, items.size());
    }

    public void add(int position, Object item) {
        if (item == null) return;
        if (position > mItems.size()) return;
        mItems.add(position, item);
        notifyItemInserted(position);
    }

    public void add(Object item) {
        if (item == null) return;
        int size = mItems.size();
        mItems.add(item);
        notifyItemInserted(size);
        mItemSelectorTool.notifyDataSetChanged(true);
    }

    public void remove(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
        mItemSelectorTool.notifyDataSetChanged(true);
    }

    public void remove(Object item) {
        int position = findPosition(item);
        if (position < 0) return;
        remove(position);
    }

    /**
     * replace all data ...
     *
     * @param items
     */
    public void replace(List<? extends Object> items) {
        if (items != null) {
            mItems.clear();
            mItems.addAll(items);
            notifyDataSetChanged();
            mItemSelectorTool.notifyDataSetChanged(true);
        }
    }

    public void clear() {
        mItems.clear();
        notifyDataSetChanged();
        mItemSelectorTool.notifyDataSetChanged(true);
    }

    @FunctionalInterface
    public interface OnItemClickListener {
        void onItemClick(int position, Object object);
    }

    /**
     * Set on item click listener.
     *
     * @param l
     */
    public void setOnItemClickListener(OnItemClickListener l) {
        mItemSelectorTool.mOnItemClickListener = l;
    }

    public int findPosition(Object item) {
        return mItems.indexOf(item);
    }

    public int getSelectedItemPosition() {
        return selectedItemPosition;
    }

    public void setSelectedItemPosition(int selectedItemPosition) {
        this.selectedItemPosition = selectedItemPosition;
    }

    @FunctionalInterface
    public interface OnBindingViewHolderListener {
        void onBindingViewHolder(BindingViewHolder holder, int position);
    }


}
