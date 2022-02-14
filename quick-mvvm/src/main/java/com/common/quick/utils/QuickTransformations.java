package com.common.quick.utils;

import com.common.quick.mvvm.QuickResource;
import com.common.quick.widget.QuickEmptyViewLayout;

import java.util.ArrayList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

/**
 * This class is collections of convenient tools for live data mapper. The idea is from {@link androidx.lifecycle.Transformations}.
 */
public class QuickTransformations {

    /**
     * Map success
     *
     * @param res
     * @param func
     * @param <Request>
     * @param <Response>
     * @param <T>
     * @return
     */
    public static <Request, Response, T> MutableLiveData<T> map(LiveData<QuickResource<Request, Response>> res, QuickResource.QuickFunction<QuickResource<Request, Response>, T> func) {
        final MediatorLiveData<T> result = new MediatorLiveData<T>();
        result.addSource(res, r -> {
            if (r.getData() != null && r.getStatus() == QuickResource.Status.READY) {
                result.setValue(func.apply(r, result.getValue()));
            }
        });
        return result;
    }


    public interface EmptyViewDataEmptyCalculator<I> {
        boolean isEmpty(I input);
    }


    /**
     * This is a convenient method for mapping @{@link QuickResource} to {@link QuickEmptyViewLayout.Data}.
     *
     * @param input
     * @param emptyCalc
     * @param <Request>
     * @param <Response>
     * @return
     */
    public static <Request, Response> MutableLiveData<QuickEmptyViewLayout.Data> mapEmptyViewData(boolean ignoreLoading, LiveData<QuickResource<Request, Response>> input, EmptyViewDataEmptyCalculator<Response> emptyCalc) {
        final MediatorLiveData<QuickEmptyViewLayout.Data> result = new MediatorLiveData<QuickEmptyViewLayout.Data>();
        result.addSource(input, r -> {
            if (r != null) {
                QuickEmptyViewLayout.Data data = result.getValue();
                if (data == null) {
                    data = QuickEmptyViewLayout.Data.build(ignoreLoading);
                }
                if (input.getValue().getLoading()) {
                    data.setLoading(true);
                } else if (input.getValue().getData() != null) { // ignore error if there is old data.
                    data.setData(input);
                    data.setEmpty(emptyCalc.isEmpty(input.getValue().getData()));
                } else if (input.getValue().getError()) {
                    data.setError(input.getValue().getThrowable());
                }
                result.setValue(data);
            }
        });
        return result;
    }

    public static <Request, Response> void connectEmptyDataLoadingWithResourceData(MutableLiveData<QuickEmptyViewLayout.Data> emptyLiveData, LiveData<QuickResource<Request, Response>> liveData) {
        liveData.observeForever(quickResource -> {
            QuickEmptyViewLayout.Data data = emptyLiveData.getValue();
            if (data != null) {
                if (quickResource.getLoading()) {
                    data.setLoading(true);
                } else {
                    data.setLoading(false);
                }
            }
            emptyLiveData.setValue(data);
        });
    }


    public interface SourcesFunction<T> {
        T map(ArrayList<LiveData> sourceValues, T old);
    }

    public static <T> MutableLiveData<T> mapSources(ArrayList<LiveData> resources, SourcesFunction<T> func) {
        final MediatorLiveData<T> result = new MediatorLiveData<T>();
        if (resources != null && resources.size() > 0) {
            for (LiveData objectLiveData : resources) {
                result.addSource(objectLiveData, r -> {
                    result.setValue(func.map(resources, result.getValue()));
                });
            }
        }
        return result;
    }
}
