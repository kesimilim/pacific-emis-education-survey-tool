package fm.doe.national.offline_sync.di;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import fm.doe.national.accreditation_core.di.AccreditationCoreComponent;
import fm.doe.national.core.preferences.GlobalPreferences;
import fm.doe.national.core.utils.LifecycleListener;
import fm.doe.national.offline_sync.data.accessor.BluetoothOfflineAccessor;
import fm.doe.national.offline_sync.data.accessor.OfflineAccessor;
import fm.doe.national.offline_sync.domain.InteractiveOfflineSyncUseCaseImpl;
import fm.doe.national.offline_sync.domain.OfflineSyncUseCase;
import fm.doe.national.wash_core.di.WashCoreComponent;

@Module
public class OfflineSyncModule {

    private final WashCoreComponent washCoreComponent;
    private final AccreditationCoreComponent accreditationCoreComponent;

    public OfflineSyncModule(WashCoreComponent washCoreComponent, AccreditationCoreComponent accreditationCoreComponent) {
        this.washCoreComponent = washCoreComponent;
        this.accreditationCoreComponent = accreditationCoreComponent;
    }

    @Provides
    @OfflineSyncScope
    OfflineAccessor provideOfflineAccessor(Context context, GlobalPreferences globalPreferences) {
        return new BluetoothOfflineAccessor(
                context,
                globalPreferences,
                accreditationCoreComponent.getDataSource(),
                washCoreComponent.getDataSource()
        );
    }

    @Provides
    @OfflineSyncScope
    OfflineSyncUseCase provideUseCase(LifecycleListener lifecycleListener, OfflineAccessor offlineAccessor) {
        return new InteractiveOfflineSyncUseCaseImpl(lifecycleListener, offlineAccessor);
    }
}
