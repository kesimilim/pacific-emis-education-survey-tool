package fm.doe.national.ui.screens.settings;

import android.graphics.Bitmap;

import com.omega_r.libs.omegatypes.Image;
import com.omega_r.libs.omegatypes.Text;
import com.omega_r.libs.omegatypes.UrlImageExtensionsKt;
import com.omegar.mvp.InjectViewState;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import fm.doe.national.R;
import fm.doe.national.app_support.MicronesiaApplication;
import fm.doe.national.cloud.model.CloudAccountData;
import fm.doe.national.cloud.model.CloudType;
import fm.doe.national.core.data.files.PicturesRepository;
import fm.doe.national.core.preferences.GlobalPreferences;
import fm.doe.national.core.ui.screens.base.BasePresenter;
import fm.doe.national.domain.SettingsInteractor;
import fm.doe.national.ui.screens.settings.items.Item;
import fm.doe.national.ui.screens.settings.items.OptionsItemFactory;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class SettingsPresenter extends BasePresenter<SettingsView> {

    private static final CloudType CLOUD_TYPE = CloudType.DRIVE;

    private final SettingsInteractor interactor = MicronesiaApplication.getInjection().getAppComponent().getSettingsInteractor();
    private final GlobalPreferences globalPreferences = MicronesiaApplication.getInjection().getCoreComponent().getGlobalPreferences();
    private final PicturesRepository picturesRepository = MicronesiaApplication.getInjection().getCoreComponent().getPicturesRepository();
    private final OptionsItemFactory itemFactory = new OptionsItemFactory();

    public SettingsPresenter() {
        refresh();
    }

    private void refresh() {
        String googleEmail = null;
        String exportFolder = null;

        if (!interactor.getConnectedAccounts().isEmpty()) {
            CloudAccountData accountData = interactor.getConnectedAccounts().get(0);
            googleEmail = accountData.getEmail();
            exportFolder = accountData.getExportPath();
        }

        Image logoImage = null;
        String logoPath = globalPreferences.getLogoPath();

        if (logoPath != null) {
            logoImage = UrlImageExtensionsKt.from(Image.Companion, logoPath);
        }

        getViewState().setItems(Arrays.asList(
                itemFactory.createLogoItem(logoImage),
                itemFactory.createAccountItem(googleEmail == null ? Text.from(R.string.label_sign_in) : Text.from(googleEmail)),
                itemFactory.createContextItem(globalPreferences.getAppRegion().getName()),
                itemFactory.createNameItem(Text.from(globalPreferences.getAppName())),
                itemFactory.createContactItem(Text.from(globalPreferences.getContactName())),
                itemFactory.createOpModeItem(globalPreferences.getOperatingMode().getName()),
                itemFactory.createImportSchoolsItem(),
                itemFactory.createExportFolderItem(exportFolder == null ? Text.empty() : Text.from(exportFolder)),
                itemFactory.createTemplatesItem(),
                itemFactory.createPasswordItem()
        ));
    }

    public void onItemPressed(Item item) {
        switch (item.getType()) {
            case ACCOUNT:
                // nothing
                break;
            case CONTACT:
                onContactPressed();
                break;
            case CONTEXT:
                onContextPressed();
                break;
            case EXPORT_FOLDER:
                onChooseFolderPressed();
                break;
            case IMPORT_SCHOOLS:
                onImportSchoolsPressed();
                break;
            case LOGO:
                onLogoPressed();
                break;
            case OP_MODE:
                onOperatingModePressed();
                break;
            case NAME:
                onNamePressed();
                break;
            case PASSWORD:
                onChangeMasterPasswordPressed();
                break;
            case TEMPLATES:
                onTemplatesPressed();
                break;
        }
    }

    private void onLogoPressed() {
        getViewState().pickPhotoFromGallery();
    }

    private void onContextPressed() {
        getViewState().showRegionSelector((region) -> {
            globalPreferences.setAppRegion(region);
            refresh();
        });
    }

    private void onNamePressed() {
        getViewState().showInputDialog(
                Text.from(R.string.title_input_name),
                Text.from(globalPreferences.getAppName()),
                (name) -> {
                    globalPreferences.setAppName(name);
                    refresh();
                }
        );
    }

    private void onContactPressed() {
        getViewState().showInputDialog(
                Text.from(R.string.title_input_contact),
                Text.from(globalPreferences.getContactName()),
                (name) -> {
                    globalPreferences.setContactName(name);
                    refresh();
                }
        );
    }

    private void onOperatingModePressed() {
        getViewState().showToast(Text.from(R.string.coming_soon));
    }

    private void onImportSchoolsPressed() {
        addDisposable(interactor.importSchools(CLOUD_TYPE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showWaiting())
                .doFinally(() -> getViewState().hideWaiting())
                .subscribe(() -> getViewState().showToast(Text.from(R.string.toast_import_schools_success)), this::handleError));
    }

    private void onChooseFolderPressed() {
        addDisposable(interactor.selectExportFolder(CLOUD_TYPE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showWaiting())
                .doFinally(() -> getViewState().hideWaiting())
                .subscribe(this::refresh, this::handleError));
    }

    private void onTemplatesPressed() {
        getViewState().showToast(Text.from(R.string.coming_soon));
    }

    private void onChangeMasterPasswordPressed() {
        getViewState().showToast(Text.from(R.string.coming_soon));
    }

    public void onImagePicked(Bitmap bitmap) {
        try {
            File pictureFile = picturesRepository.createEmptyFile();
            FileOutputStream fos = new FileOutputStream(pictureFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            bitmap.recycle();
            String filePath = pictureFile.getPath();
            globalPreferences.setLogoPath(filePath);
            refresh();
        } catch (IOException ex) {
            ex.printStackTrace();
            getViewState().showMessage(Text.from(R.string.title_error), Text.from(R.string.error_save_logo));
        }
    }

}
