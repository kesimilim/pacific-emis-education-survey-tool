package fm.doe.national.ui.screens.report.summary;

import com.omegar.mvp.InjectViewState;

import fm.doe.national.app_support.MicronesiaApplication;
import fm.doe.national.domain.ReportInteractor;
import fm.doe.national.ui.screens.base.BasePresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class SummaryPresenter extends BasePresenter<SummaryView> {

    private final ReportInteractor interactor = MicronesiaApplication.getAppComponent().getReportInteractor();

    public SummaryPresenter() {
        loadSummary();
    }

    private void loadSummary() {
        addDisposable(interactor.getSummarySubject()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().setLoadingVisibility(true))
                .doFinally(() -> getViewState().setLoadingVisibility(false))
                .subscribe(getViewState()::setSummaryData, this::handleError));
    }
}
