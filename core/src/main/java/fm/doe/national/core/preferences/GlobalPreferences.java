package fm.doe.national.core.preferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import fm.doe.national.core.preferences.entities.AppRegion;
import fm.doe.national.core.preferences.entities.OperatingMode;
import fm.doe.national.core.preferences.entities.SurveyType;

public interface GlobalPreferences {

    @NonNull
    AppRegion getAppRegion();

    boolean isAppRegionSaved();

    void setAppRegion(AppRegion appRegion);

    @NonNull
    SurveyType getSurveyTypeOrDefault();

    @Nullable
    SurveyType getSurveyType();

    void setSurveyType(SurveyType surveyType);

    @Nullable
    String getLogoPath();

    void setLogoPath(@Nullable String path);

    String getMasterPassword();

    boolean isMasterPasswordSaved();

    void setMasterPassword(String password);

    String getFactoryPassword();

    String getAppName();

    void setAppName(String name);

    String getContactName();

    void setContactName(String name);

    OperatingMode getOperatingMode();

    void setOperatingMode(OperatingMode mode);

}