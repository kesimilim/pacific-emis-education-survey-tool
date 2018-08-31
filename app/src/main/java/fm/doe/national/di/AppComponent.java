package fm.doe.national.di;

import android.content.SharedPreferences;

import java.util.List;

import javax.inject.Singleton;

import dagger.Component;
import fm.doe.national.data.cloud.CloudRepository;
import fm.doe.national.data.cloud.drive.DriveCloudAccessor;
import fm.doe.national.data.cloud.drive.DriveCloudPreferences;
import fm.doe.national.data.cloud.dropbox.DropboxCloudAccessor;
import fm.doe.national.data.cloud.dropbox.DropboxCloudPreferences;
import fm.doe.national.data.data_source.DataSource;
import fm.doe.national.data.data_source.models.School;
import fm.doe.national.data.data_source.models.SchoolAccreditation;
import fm.doe.national.data.data_source.models.serializable.LinkedSchoolAccreditation;
import fm.doe.national.data.parsers.Parser;
import fm.doe.national.data.serializers.Serializer;
import fm.doe.national.di.modules.AccreditationDataSourceModule;
import fm.doe.national.di.modules.CloudModule;
import fm.doe.national.di.modules.ContextModule;
import fm.doe.national.di.modules.DatabaseHelperModule;
import fm.doe.national.di.modules.GsonModule;
import fm.doe.national.di.modules.InteractorsModule;
import fm.doe.national.di.modules.LifecycleModule;
import fm.doe.national.di.modules.ParsersModule;
import fm.doe.national.di.modules.SerializersModule;
import fm.doe.national.di.modules.SharedPreferencesModule;
import fm.doe.national.domain.SettingsInteractor;
import fm.doe.national.ui.screens.cloud.DriveActivity;
import fm.doe.national.ui.screens.group_standards.GroupStandardsPresenter;
import fm.doe.national.ui.screens.school_accreditation.SchoolAccreditationPresenter;
import fm.doe.national.ui.screens.splash.SplashPresenter;
import fm.doe.national.ui.screens.standard.StandardPresenter;
import fm.doe.national.ui.screens.survey_creation.CreateSurveyPresenter;
import fm.doe.national.utils.LifecycleListener;

@Singleton
@Component(modules = {
        ContextModule.class,
        DatabaseHelperModule.class,
        AccreditationDataSourceModule.class,
        GsonModule.class,
        CloudModule.class,
        SharedPreferencesModule.class,
        ParsersModule.class,
        SerializersModule.class,
        LifecycleModule.class,
        InteractorsModule.class})
public interface AppComponent {
    Parser<LinkedSchoolAccreditation> getSchoolAccreditationParser();
    Parser<List<School>> getSchoolsParser();
    Serializer<LinkedSchoolAccreditation> getSchoolAccreditationSerizlizer();
    SharedPreferences getSharedPreferences();
    DropboxCloudPreferences getDropboxCloudPreferences();
    DriveCloudPreferences getDriveCloudPreferences();
    DriveCloudAccessor getDriveCloudAccessor();
    DropboxCloudAccessor getDropboxCloudAccessor();
    LifecycleListener getLifecycleListener();
    CloudRepository getCloudRepository();
    DataSource getDataSource();
    SettingsInteractor getSettingsInteractor();
    void inject(DriveActivity target);
    void inject(StandardPresenter standardPresenter);
    void inject(SplashPresenter splashPresenter);
    void inject(SchoolAccreditationPresenter schoolAccreditationPresenter);
    void inject(CreateSurveyPresenter createSurveyPresenter);
    void inject(GroupStandardsPresenter groupStandardsPresenter);
}