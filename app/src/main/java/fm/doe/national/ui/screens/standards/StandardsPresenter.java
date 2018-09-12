package fm.doe.national.ui.screens.standards;

import com.arellomobile.mvp.InjectViewState;

import fm.doe.national.MicronesiaApplication;
import fm.doe.national.data.data_source.DataSource;
import fm.doe.national.data.data_source.models.CategoryProgress;
import fm.doe.national.data.data_source.models.Standard;
import fm.doe.national.ui.screens.base.BasePresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class StandardsPresenter extends BasePresenter<StandardsView> {

    private final long passingId;
    private final long categoryId;
    private final DataSource dataSource = MicronesiaApplication.getAppComponent().getDataSource();

    public StandardsPresenter(long passingId, long categoryId) {
        this.passingId = passingId;
        this.categoryId = categoryId;
        loadPassing();
        loadStandards();
    }

    private void loadPassing() {
        addDisposable(dataSource.requestSchoolAccreditationPassing(passingId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showWaiting())
                .doFinally(() -> getViewState().hideWaiting())
                .doOnSuccess(passing -> getViewState().setSurveyYear(passing.getYear()))
                .flatMap(passing -> dataSource.requestGroupStandards(passingId))
                .toObservable()
                .flatMapIterable(it -> it)
                .filter(category -> category.getId() == categoryId)
                .subscribe(category -> getViewState().setCategoryName(category.getName()), this::handleError));
    }

    private void loadStandards() {
        addDisposable(dataSource.requestStandards(passingId, categoryId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showWaiting())
                .doFinally(() -> getViewState().hideWaiting())
                .subscribe(standards -> {
                    StandardsView view = getViewState();
                    view.showStandards(standards);

                    int completedCount = 0;
                    for (Standard standard : standards) {
                        CategoryProgress progress = standard.getCategoryProgress();
                        if (progress.getAnsweredQuestionsCount() == progress.getTotalQuestionsCount()) completedCount++;
                    }
                    view.setGlobalProgress(completedCount, standards.size());
                }, this::handleError));
    }

    public void onStandardClicked(Standard standard) {
        getViewState().navigateToCriteriasScreen(passingId, categoryId, standard.getId());
    }
}
