package fm.doe.national.rmi_report.di;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.accreditation_core.di.AccreditationCoreComponent;
import fm.doe.national.rmi_report.domain.RmiReportInteractor;
import fm.doe.national.rmi_report.domain.RmiReportInteractorImpl;
import fm.doe.national.rmi_report.domain.RmiReportsProvider;
import fm.doe.national.rmi_report.domain.RmiReportsProviderImpl;

@Module
public class RmiReportModule {

    private final AccreditationCoreComponent accreditationCoreComponent;

    public RmiReportModule(AccreditationCoreComponent accreditationCoreComponent) {
        this.accreditationCoreComponent = accreditationCoreComponent;
    }

    @Provides
    @RmiReportScope
    RmiReportInteractor provideRmiReportInteractor() {
        return new RmiReportInteractorImpl();
    }

    @Provides
    @RmiReportScope
    RmiReportsProvider provideRmiReportsProvider(RmiReportInteractor interactor) {
        return new RmiReportsProviderImpl(interactor, accreditationCoreComponent.getAccreditationSurveyInteractor());
    }
}