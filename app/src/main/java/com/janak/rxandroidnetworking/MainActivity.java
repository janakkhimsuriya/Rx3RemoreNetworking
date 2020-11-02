package com.janak.rxandroidnetworking;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import com.janak.androidnetworking.interfaces.AnalyticsListener;
import com.janak.androidnetworking.networking.Rx3RemoteNetworking;
import com.janak.rxandroidnetworking.model.ApiUser;
import com.janak.rxandroidnetworking.model.User;
import com.janak.rxandroidnetworking.model.UserDetail;
import com.janak.rxandroidnetworking.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.BiFunction;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.functions.Predicate;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testApi();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /************************************
     * Just an test api start
     ************************************/

    private void testApi() {

        Single<List<User>> single = Rx3RemoteNetworking.get("https://fierce-cove-29863.herokuapp.com/getAllUsers/{pageNumber}")
                .addPathParameter("pageNumber", "0")
                .addQueryParameter("limit", "3")
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                        Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                        Log.d(TAG, " bytesSent : " + bytesSent);
                        Log.d(TAG, " bytesReceived : " + bytesReceived);
                        Log.d(TAG, " isFromCache : " + isFromCache);
                    }
                })
                .getObjectListSingle(User.class);

        // first observer
        single.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<User>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable disposable) {
                        Log.d(TAG + "_1", "onSubscribe");
                    }

                    @Override
                    public void onSuccess(@NonNull List<User> users) {
                        Log.d(TAG + "_1", "userList size : " + users.size());
                        for (User user : users) {
                            Log.d(TAG, "id : " + user.id);
                            Log.d(TAG, "firstname : " + user.firstname);
                            Log.d(TAG, "lastname : " + user.lastname);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable throwable) {
                        Utils.logError(TAG + "_1", throwable);
                    }
                });

        // second observer
        single.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<User>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable disposable) {
                        Log.d(TAG + "_2", "onSubscribe");
                    }

                    @Override
                    public void onSuccess(@NonNull List<User> users) {
                        Log.d(TAG + "_2", "userList size : " + users.size());
                        for (User user : users) {
                            Log.d(TAG, "id : " + user.id);
                            Log.d(TAG, "firstname : " + user.firstname);
                            Log.d(TAG, "lastname : " + user.lastname);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable throwable) {
                        Utils.logError(TAG + "_2", throwable);
                    }
                });
    }

    /************************************
     * map operator start
     ************************************/

    public void map(View view) {
        Rx3RemoteNetworking.get("https://fierce-cove-29863.herokuapp.com/getAnUser/{userId}")
                .addPathParameter("userId", "1")
                .build()
                .getObjectSingle(ApiUser.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<ApiUser, User>() {
                    @Override
                    public User apply(ApiUser apiUser) throws Exception {
                        // here we get ApiUser from server
                        User user = new User(apiUser);
                        // then by converting, we are returning user
                        return user;
                    }
                })
                .subscribe(new SingleObserver<User>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable disposable) {

                    }

                    @Override
                    public void onSuccess(@NonNull User user) {
                        Log.d(TAG, "user id : " + user.id);
                        Log.d(TAG, "user firstname : " + user.firstname);
                        Log.d(TAG, "user lastname : " + user.lastname);
                    }

                    @Override
                    public void onError(@NonNull Throwable throwable) {
                        Utils.logError(TAG, throwable);
                    }
                });
    }


    /************************************
     * zip operator start
     *********************************/

    /*
     * This observable return the list of User who loves cricket
     */
    private Observable<List<User>> getCricketFansObservable() {
        return Rx3RemoteNetworking.get("https://fierce-cove-29863.herokuapp.com/getAllCricketFans")
                .build()
                .getObjectListObservable(User.class);
    }

    /*
     * This observable return the list of User who loves Football
     */
    private Observable<List<User>> getFootballFansObservable() {
        return Rx3RemoteNetworking.get("https://fierce-cove-29863.herokuapp.com/getAllFootballFans")
                .build()
                .getObjectListObservable(User.class);
    }

    /*
     * This do the complete magic, make both network call
     * and then returns the list of user who loves both
     * Using zip operator to get both response at a time
     */
    private void findUsersWhoLovesBoth() {
        // here we are using zip operator to combine both request
        Observable.zip(getCricketFansObservable(), getFootballFansObservable(),
                new BiFunction<List<User>, List<User>, List<User>>() {
                    @Override
                    public List<User> apply(List<User> cricketFans, List<User> footballFans) throws Exception {
                        List<User> userWhoLovesBoth =
                                filterUserWhoLovesBoth(cricketFans, footballFans);
                        return userWhoLovesBoth;
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<User>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<User> users) {
                        // do anything with user who loves both
                        Log.d(TAG, "userList size : " + users.size());
                        for (User user : users) {
                            Log.d(TAG, "id : " + user.id);
                            Log.d(TAG, "firstname : " + user.firstname);
                            Log.d(TAG, "lastname : " + user.lastname);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Utils.logError(TAG, e);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");
                    }
                });
    }

    private List<User> filterUserWhoLovesBoth(List<User> cricketFans, List<User> footballFans) {
        List<User> userWhoLovesBoth = new ArrayList<>();
        for (User cricketFan : cricketFans) {
            for (User footballFan : footballFans) {
                if (cricketFan.id == footballFan.id) {
                    userWhoLovesBoth.add(cricketFan);
                }
            }
        }
        return userWhoLovesBoth;
    }


    public void zip(View view) {
        findUsersWhoLovesBoth();
    }

    /************************************
     * flatMap and filter operator start
     ************************************/

    private Observable<List<User>> getAllMyFriendsObservable() {
        return Rx3RemoteNetworking.get("https://fierce-cove-29863.herokuapp.com/getAllFriends/{userId}")
                .addPathParameter("userId", "1")
                .build()
                .getObjectListObservable(User.class);
    }

    public void flatMapAndFilter(View view) {
        getAllMyFriendsObservable()
                .flatMap(new Function<List<User>, ObservableSource<User>>() { // flatMap - to return users one by one
                    @Override
                    public ObservableSource<User> apply(List<User> usersList) throws Exception {
                        return Observable.fromIterable(usersList); // returning user one by one from usersList.
                    }
                })
                .filter(new Predicate<User>() {
                    @Override
                    public boolean test(User user) throws Exception {
                        // filtering user who follows me.
                        return user.isFollowing;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(User user) {
                        // only the user who is following me comes here one by one
                        Log.d(TAG, "user id : " + user.id);
                        Log.d(TAG, "user firstname : " + user.firstname);
                        Log.d(TAG, "user lastname : " + user.lastname);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Utils.logError(TAG, e);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");
                    }
                });
    }

    /************************************
     * take operator start
     ************************************/

    public void take(View view) {
        getUserListObservable()
                .flatMap(new Function<List<User>, ObservableSource<User>>() { // flatMap - to return users one by one
                    @Override
                    public ObservableSource<User> apply(List<User> usersList) throws Exception {
                        return Observable.fromIterable(usersList); // returning user one by one from usersList.
                    }
                })
                .take(4) // it will only emit first 4 users out of all
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(User user) {
                        // // only four user comes here one by one
                        Log.d(TAG, "user id : " + user.id);
                        Log.d(TAG, "user firstname : " + user.firstname);
                        Log.d(TAG, "user lastname : " + user.lastname);
                        Log.d(TAG, "isFollowing : " + user.isFollowing);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Utils.logError(TAG, e);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");
                    }
                });
    }


    /************************************
     * flatMap operator start
     ************************************/


    public void flatMap(View view) {
        getUserListObservable()
                .flatMap(new Function<List<User>, ObservableSource<User>>() { // flatMap - to return users one by one
                    @Override
                    public ObservableSource<User> apply(List<User> usersList) throws Exception {
                        return Observable.fromIterable(usersList); // returning user one by one from usersList.
                    }
                })
                .flatMap(new Function<User, ObservableSource<UserDetail>>() {
                    @Override
                    public ObservableSource<UserDetail> apply(User user) throws Exception {
                        // here we get the user one by one
                        // and returns corresponding getUserDetailObservable
                        // for that userId
                        return getUserDetailObservable(user.id);
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserDetail>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Utils.logError(TAG, e);
                    }

                    @Override
                    public void onNext(UserDetail userDetail) {
                        // do anything with userDetail
                        Log.d(TAG, "userDetail id : " + userDetail.id);
                        Log.d(TAG, "userDetail firstname : " + userDetail.firstname);
                        Log.d(TAG, "userDetail lastname : " + userDetail.lastname);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");
                    }
                });
    }

    /************************************
     * flatMapWithZip operator start
     ************************************/

    private Observable<List<User>> getUserListObservable() {
        return Rx3RemoteNetworking.get("https://fierce-cove-29863.herokuapp.com/getAllUsers/{pageNumber}")
                .addPathParameter("pageNumber", "0")
                .addQueryParameter("limit", "10")
                .build()
                .getObjectListObservable(User.class);
    }

    private Observable<UserDetail> getUserDetailObservable(long id) {
        return Rx3RemoteNetworking.get("https://fierce-cove-29863.herokuapp.com/getAnUserDetail/{userId}")
                .addPathParameter("userId", String.valueOf(id))
                .build()
                .getObjectObservable(UserDetail.class);
    }

    public void flatMapWithZip(View view) {
        getUserListObservable()
                .flatMap(new Function<List<User>, ObservableSource<User>>() { // flatMap - to return users one by one
                    @Override
                    public ObservableSource<User> apply(List<User> usersList) throws Exception {
                        return Observable.fromIterable(usersList); // returning user one by one from usersList.
                    }
                })
                .flatMap(new Function<User, ObservableSource<Pair<UserDetail, User>>>() {
                    @Override
                    public ObservableSource<Pair<UserDetail, User>> apply(User user) throws Exception {
                        // here we get the user one by one and then we are zipping
                        // two observable - one getUserDetailObservable (network call to get userDetail)
                        // and another Observable.just(user) - just to emit user
                        return Observable.zip(getUserDetailObservable(user.id),
                                Observable.just(user),
                                new BiFunction<UserDetail, User, Pair<UserDetail, User>>() {
                                    @Override
                                    public Pair<UserDetail, User> apply(UserDetail userDetail, User user) throws Exception {
                                        // runs when network call completes
                                        // we get here userDetail for the corresponding user
                                        return new Pair<>(userDetail, user); // returning the pair(userDetail, user)
                                    }
                                });
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Pair<UserDetail, User>>() {
                    @Override
                    public void onComplete() {
                        // do something onCompleted
                        Log.d(TAG, "onComplete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        // handle error
                        Utils.logError(TAG, e);
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Pair<UserDetail, User> pair) {
                        // here we are getting the userDetail for the corresponding user one by one
                        UserDetail userDetail = pair.first;
                        User user = pair.second;
                        Log.d(TAG, "userId : " + user.id);
                        Log.d(TAG, "userDetail firstname : " + userDetail.firstname);
                        Log.d(TAG, "userDetail lastname : " + userDetail.lastname);
                    }
                });
    }

    /************************************
     * others start here
     ************************************/

    public void startRxApiTestActivity(View view) {
        startActivity(new Intent(MainActivity.this, UserDetailActivity.class));
    }

    public void startSubscriptionActivity(View view) {
        startActivity(new Intent(MainActivity.this, DownloadImageActivity.class));
    }

}