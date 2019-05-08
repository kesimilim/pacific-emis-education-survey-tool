package fm.doe.national.ui.screens.school_accreditation;

import com.omegar.mvp.InjectViewState;

import java.util.ArrayList;
import java.util.List;

import fm.doe.national.MicronesiaApplication;
import fm.doe.national.data.data_source.DataSource;
import fm.doe.national.data.model.mutable.MutableSurvey;
import fm.doe.national.domain.SurveyInteractor;
import fm.doe.national.ui.screens.menu.drawer.BaseDrawerPresenter;
import fm.doe.national.utils.CollectionUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class SchoolAccreditationPresenter extends BaseDrawerPresenter<SchoolAccreditationView> {

    private final SurveyInteractor interactor = MicronesiaApplication.getAppComponent().getSurveyInteractor();
    private final DataSource dataSource = MicronesiaApplication.getAppComponent().getDataSource();

    private List<MutableSurvey> surveys = new ArrayList<>();

    private MutableSurvey passingToDelete;

    @Override
    public void attachView(SchoolAccreditationView view) {
        super.attachView(view);
        loadRecentPassings();
    }

    private void loadRecentPassings() {

        addDisposable(interactor.getAllSurveys()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showWaiting())
                .doFinally(() -> getViewState().hideWaiting())
                .subscribe(surveys -> {
                    this.surveys = surveys;
                    getViewState().setAccreditations(CollectionUtils.map(this.surveys, item -> item));
                }, this::handleError));
    }

    public void onAccreditationClicked(MutableSurvey schoolAccreditationPassing) {
        interactor.setCurrentSurvey(schoolAccreditationPassing);
        getViewState().navigateToCategoryChooser();
    }

    public void onSearchQueryChanged(String query) {
        List<MutableSurvey> queriedPassings = new ArrayList<>();
        String lowerQuery = query.toLowerCase();
        for (MutableSurvey passing : surveys) {
            if (passing.getSchoolName().toLowerCase().contains(lowerQuery) ||
                    passing.getSchoolId().toLowerCase().contains(lowerQuery)) {
                queriedPassings.add(passing);
            }
        }
        getViewState().setAccreditations(queriedPassings);
    }

    public void onAccreditationLongClicked(MutableSurvey item) {
        passingToDelete = item;
        getViewState().showSurveyDeleteConfirmation();
    }

    public void onSurveyDeletionConfirmed() {
        addDisposable(dataSource.deleteSurvey(passingToDelete.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> getViewState().removeSurveyPassing(passingToDelete), this::handleError));
    }
}
