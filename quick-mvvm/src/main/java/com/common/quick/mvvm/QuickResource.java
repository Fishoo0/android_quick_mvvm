package com.common.quick.mvvm;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;


/**
 * Why named Resource? @guochunmao
 * <p>
 * This tool is a wrapper for request & response.
 * <p>
 * What can i do:
 * 1, Loading {@link #getLoading()}
 * 2, Data {@link #getSuccess()}
 * 3, Error {@link #getError()}
 *
 * @param <Response>
 */
public class QuickResource<Request, Response> {

    private static final Handler sHandler = new Handler(Looper.getMainLooper());

    public enum Status {
        NONE,
        LOADING,
        READY,
        ERROR
    }

    private Status mStatus;
    private Request mRequest;
    private Response mData;
    private Throwable mError;
    private Runnable mRetry;

    private QuickResource(Status status, Request request, Response data, Throwable th, Runnable retry) {
        mStatus = status;
        mRequest = request;
        mData = data;
        mError = th;
        mRetry = retry;
    }

    /**
     * Clone structure.
     *
     * @param resource
     */
    private QuickResource(QuickResource<Request, Response> resource) {
        this(resource.getStatus(), resource.getRequest(), resource.getData(), resource.getThrowable(), resource.getRetry());
    }

    public Status getStatus() {
        return mStatus;
    }

    public Response getData() {
        return mData;
    }

    public Throwable getThrowable() {
        return mError;
    }

    public boolean getLoading() {
        return mStatus == Status.LOADING;
    }

    public boolean getError() {
        return mStatus == Status.ERROR;
    }

    public boolean getSuccess() {
        return !getError() && getData() != null;
    }

    public Request getRequest() {
        return mRequest;
    }

    public Runnable getRetry() {
        return mRetry;
    }

    public QuickResource<Request, Response> toLoading(Request request) {
        mRequest = request;
        mStatus = Status.LOADING;
        return this;
    }

    public QuickResource<Request, Response> toSuccess(Response data) {
        mData = data;
        mStatus = Status.READY;
        mError = null;
        return this;
    }

    public QuickResource<Request, Response> toError(Throwable th) {
        mStatus = Status.ERROR;
        mError = th;
        return this;
    }

    public static <Response> QuickResource<Boolean, Response> loading() {
        return loading(null, true, null);
    }

    public static <Request, Response> QuickResource<Request, Response> loading(@NonNull Request request) {
        return loading(null, request, null);
    }

    public static <Request, Response> QuickResource<Request, Response> loading(@NonNull Request request, Runnable retry) {
        return loading(null, request, retry);
    }

    public static <Request, Response> QuickResource<Request, Response> loading(QuickResource<Request, Response> last, @NonNull Request request) {
        return loading(last, request, null);
    }

    public static <Request, Response> QuickResource<Request, Response> loading(QuickResource<Request, Response> last, @NonNull Request request, Runnable retry) {
        if (request == null) {
            throw new IllegalArgumentException("request can not be null !");
        }
        QuickResource<Request, Response> resource = new QuickResource(
                Status.LOADING,
                request != null ? request : (last != null ? last.mRequest : null),
                last != null ? last.mData : null,
                last != null ? last.mError : null,
                retry != null ? retry : (last != null ? last.mRetry : null));
        return resource;
    }

    public static <Request, Response> QuickResource<Request, Response> success(@NonNull QuickResource<Request, Response> last, @NonNull Response data) {
        if (last == null || data == null) {
            throw new IllegalArgumentException("last or Data can not be null !");
        }
        QuickResource<Request, Response> resource = new QuickResource(Status.LOADING, last.mRequest, data, null, last.mRetry);
        return resource;
    }

    public static <Request, Response> QuickResource<Request, Response> error(@NonNull QuickResource<Request, Response> last, @NonNull Throwable th) {
        if (last == null || th == null) {
            throw new IllegalArgumentException("last or Data can not be null !");
        }
        QuickResource<Request, Response> resource = new QuickResource(Status.ERROR, last.mRequest, last.mData, th, last.mRetry);
        return resource;
    }

    /**
     * Represents a function.
     *
     * @param <I> the type of the input to the function
     * @param <O> the type of the output of the function
     */
    public interface QuickFunction<I, O> {
        /**
         * Applies this function to the given input.
         *
         * @param input the input
         * @return the function result.
         */
        O apply(I input, O old);
    }


    public interface QuickFunction1<I, O> {
        /**
         * Applies this function to the given input.
         *
         * @param input the input
         * @return the function result.
         */
        O apply(I input);
    }

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
    public static <Request, Response, T> MutableLiveData<T> mapSuccess(LiveData<QuickResource<Request, Response>> res, QuickFunction<QuickResource<Request, Response>, T> func) {
        final MediatorLiveData<T> result = new MediatorLiveData<T>();
        result.addSource(res, r -> {
            if (r.getData() != null && r.getStatus() == Status.READY) {
                result.setValue(func.apply(r, result.getValue()));
            }
        });
        return result;
    }


    public static <Request, Response, T> MutableLiveData<T> mapSuccess(LiveData<QuickResource<Request, Response>> res, QuickFunction1<QuickResource<Request, Response>, T> func) {
        final MediatorLiveData<T> result = new MediatorLiveData<T>();
        result.addSource(res, r -> {
            if (r.getData() != null && r.getStatus() == Status.READY) {
                result.setValue(func.apply(r));
            }
        });
        return result;
    }

    public static <Request, Response, T> MutableLiveData<T> map(LiveData<QuickResource<Request, Response>> res, QuickFunction<QuickResource<Request, Response>, T> func) {
        final MediatorLiveData<T> result = new MediatorLiveData<T>();
        result.addSource(res, r -> {
            result.setValue(func.apply(r, result.getValue()));
        });
        return result;
    }

}
