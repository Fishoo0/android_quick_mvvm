package com.common.quick.bindingadapter;

import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

/**
 * RecyclerView 的 databinding adapter
 */
public class RecycleViewBindingAdapter {

    /**
     * Create adapter for RecycleView
     *
     * @param recyclerView
     * @return
     */
    private static RecyclerView.Adapter getAdapter(RecyclerView recyclerView) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter == null) {
            adapter = new RecycleViewAdapter(recyclerView);
            recyclerView.setAdapter(adapter);
        }

        return adapter;
    }

    @androidx.databinding.BindingAdapter("template")
    public static <T> void setRecyclerViewTemplate(RecyclerView recyclerView, T template) {
        RecyclerView.Adapter adapter = getAdapter(recyclerView);
        if (adapter instanceof RecycleViewAdapter) {
            if (template instanceof Integer) {
                ((RecycleViewAdapter) adapter).setItemBinding(new RecycleViewAdapter.BaseItemBinding(recyclerView.getContext(), (Integer) template));
            } else if (template instanceof RecycleViewAdapter.ItemBinding) {
                ((RecycleViewAdapter) adapter).setItemBinding((RecycleViewAdapter.ItemBinding) template);
            } else if (template instanceof Map) {
                RecycleViewAdapter.BaseMultipleTypeItemBinding baseMultipleTypeItemBinding = new RecycleViewAdapter.BaseMultipleTypeItemBinding(recyclerView.getContext(), (Map<Class, Integer>) template);
                ((RecycleViewAdapter) adapter).setItemBinding(baseMultipleTypeItemBinding);
            }
        }
    }

    @androidx.databinding.BindingAdapter("data")
    public static <T> void setRecyclerViewData(RecyclerView recyclerView, T data) {
        RecyclerView.Adapter adapter = getAdapter(recyclerView);
        if (adapter instanceof RecycleViewAdapter) {
            if (data instanceof List) {
                ((RecycleViewAdapter) adapter).replace((List) data);
            }
        }
    }

    @androidx.databinding.BindingAdapter("itemClick")
    public static <T> void setOnItemClickListener(RecyclerView recyclerView, RecycleViewAdapter.OnItemClickListener listener) {
        RecyclerView.Adapter adapter = getAdapter(recyclerView);
        if (adapter instanceof RecycleViewAdapter) {
            ((RecycleViewAdapter) adapter).setOnItemClickListener(listener);
        }
    }

    @androidx.databinding.BindingAdapter({"adapterTag"})
    public static void debugTag(RecyclerView recyclerView, String tag) {
        RecycleViewAdapter adapter = (RecycleViewAdapter) getAdapter(recyclerView);
        adapter.TAG = tag;
    }

    /**
     * See {@link #setSelectFirst(RecyclerView, int)} for detail.
     *
     * @param recyclerView
     * @param setSelectFirst true = #setSelectFirst(RecyclerView, 0); false = #setSelectFirst(RecyclerView, -2).
     * @param <T>
     */
    @androidx.databinding.BindingAdapter({"selectFirst"})
    public static <T> void setSelectFirst(RecyclerView recyclerView, boolean setSelectFirst) {
        RecycleViewAdapter adapter = (RecycleViewAdapter) getAdapter(recyclerView);
        adapter.mItemSelectorTool.mInitSelectPosition = setSelectFirst ? 0 : -2;
    }

    /**
     * Note: It's your responsibility to control the scroll position.
     *
     * @param recyclerView
     * @param position     -1: Auto select the first enabled item on sight, note "on sight", not always the first one.; 0...N target position.
     * @param <T>
     */
    @androidx.databinding.BindingAdapter({"selectFirst"})
    public static <T> void setSelectFirst(RecyclerView recyclerView, int position) {
        RecycleViewAdapter adapter = (RecycleViewAdapter) getAdapter(recyclerView);
        adapter.mItemSelectorTool.mInitSelectPosition = position;
    }

    @androidx.databinding.BindingAdapter(value = {"scrollToPositionWithOffsetPosition", "scrollToPositionWithOffsetOffset"}, requireAll = false)
    public static <T> void bindScrollToPosition(RecyclerView recyclerView, int position, int offset) {
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (position >= 0 && position < recyclerView.getAdapter().getItemCount()) {
                    if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                        ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPositionWithOffset(position, offset);
                    }
                }
            }
        }, 0);
    }

    @androidx.databinding.BindingAdapter({"enableSelector"})
    public static void enableSelector(RecyclerView recyclerView, boolean enable) {
        RecycleViewAdapter adapter = (RecycleViewAdapter) getAdapter(recyclerView);
        adapter.mItemSelectorTool.mEnableSelector = enable;
    }

    @androidx.databinding.BindingAdapter({"enableSelector"})
    public static void enableSelector(View view, boolean enable) {
        view.setEnabled(enable);
    }

    @androidx.databinding.BindingAdapter({"itemBRObject"})
    public static void bindItemBRObject(RecyclerView recyclerView, Object object) {
        RecycleViewAdapter adapter = (RecycleViewAdapter) getAdapter(recyclerView);
        adapter.mObject = object;
    }

    @androidx.databinding.BindingAdapter({"itemBRView"})
    public static void bindItemBRView(RecyclerView recyclerView, Object view) {
        RecycleViewAdapter adapter = (RecycleViewAdapter) getAdapter(recyclerView);
        adapter.mView = view;
    }

    @androidx.databinding.BindingAdapter("maxLines")
    public static void setRecyclerViewMaxLines(RecyclerView recyclerView, int num) {
        RecyclerView.Adapter adapter = getAdapter(recyclerView);
        if (adapter instanceof RecycleViewAdapter) {
            ((RecycleViewAdapter) adapter).mMaxLines = num;
        }
    }

    @androidx.databinding.BindingAdapter(value = {"layoutManagerFactory", "adapter"}, requireAll = false)
    public static void setRecyclerLayoutManager(RecyclerView view, LayoutManagerFactory factory, RecyclerView.Adapter adapter) {
        if (factory != null)
            view.setLayoutManager(factory.create(view));
        if (adapter != null)
            view.setAdapter(adapter);
    }

    @androidx.databinding.BindingAdapter("itemDecoration")
    public static void setRecyclerItem(RecyclerView view, RecyclerView.ItemDecoration decoration) {
        view.addItemDecoration(decoration);
    }

    @androidx.databinding.BindingAdapter("hasFixedSize")
    public static void setRecyclerHasFixedSize(RecyclerView view, boolean hasFixedSize) {
        view.setHasFixedSize(hasFixedSize);
    }

//    @androidx.databinding.BindingAdapter("onScrollToBottom")
//    public static void setRecyclerViewScrollToBottom(RecyclerView view, BindingAction action) {
//        view.addOnScrollListener(new OnRecyclerViewScrollListener(action));
//    }
//
//    public static class OnRecyclerViewScrollListener extends RecyclerView.OnScrollListener {
//
//        private BindingAction mScrollToEndAction;
//
//        public OnRecyclerViewScrollListener(BindingAction end) {
//            this.mScrollToEndAction = end;
//        }
//
//        @Override
//        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//            if (newState != RecyclerView.SCROLL_STATE_IDLE) return;
//
//            RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
//
//            if (manager instanceof LinearLayoutManager) {
//                LinearLayoutManager lManager = (LinearLayoutManager) manager;
//                if (lManager.getOrientation() == LinearLayoutManager.HORIZONTAL) {
//                    if (!recyclerView.canScrollHorizontally(1)) {
//                        if (mScrollToEndAction != null) mScrollToEndAction.call();
//                    }
//                } else {
//                    if (!recyclerView.canScrollVertically(1)) {
//                        if (mScrollToEndAction != null) mScrollToEndAction.call();
//                    }
//                }
//            }
//        }
//    }

    public static class LayoutManagers {
        public static LayoutManagerFactory linear() {
            return new LayoutManagerFactory() {
                @Override
                public RecyclerView.LayoutManager create(RecyclerView view) {
                    return new LinearLayoutManager(view.getContext());
                }
            };
        }

        public static LayoutManagerFactory linear(@RecyclerView.Orientation final int orientation, final boolean reverse) {
            return new LayoutManagerFactory() {
                @Override
                public RecyclerView.LayoutManager create(RecyclerView view) {
                    return new LinearLayoutManager(view.getContext(), orientation, reverse);
                }
            };
        }

        public static LayoutManagerFactory grid(final int spanCount) {
            return new LayoutManagerFactory() {
                @Override
                public RecyclerView.LayoutManager create(RecyclerView view) {
                    return new GridLayoutManager(view.getContext(), spanCount);
                }
            };
        }

        public static LayoutManagerFactory grid(final int spanCount, @RecyclerView.Orientation final int orientation, final boolean reverse) {
            return new LayoutManagerFactory() {
                @Override
                public RecyclerView.LayoutManager create(RecyclerView view) {
                    return new GridLayoutManager(view.getContext(), spanCount, orientation, reverse);
                }
            };
        }
    }

    public static interface LayoutManagerFactory {
        RecyclerView.LayoutManager create(RecyclerView view);
    }
}
