package fm.doe.national.wash_core.data.serialization.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.convert.Convert;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import fm.doe.national.core.data.model.BaseSerializableIdentifiedObject;
import fm.doe.national.core.data.model.Progress;
import fm.doe.national.core.data.model.SurveyState;
import fm.doe.national.core.data.serialization.converters.AppRegionConverter;
import fm.doe.national.core.data.serialization.converters.SurveyStateConverter;
import fm.doe.national.core.data.serialization.converters.SurveyTypeConverter;
import fm.doe.national.core.data.serialization.converters.UtcDateConverter;
import fm.doe.national.core.preferences.entities.AppRegion;
import fm.doe.national.core.preferences.entities.SurveyType;
import fm.doe.national.core.utils.ObjectUtils;
import fm.doe.national.wash_core.data.model.Group;
import fm.doe.national.wash_core.data.model.WashSurvey;

@Root(name = "survey")
public class SerializableWashSurvey extends BaseSerializableIdentifiedObject implements WashSurvey {

    @Element
    @Convert(SurveyTypeConverter.class)
    SurveyType type;

    @Element(name = "country")
    @Convert(AppRegionConverter.class)
    AppRegion region;

    @Element
    int version;

    @Nullable
    @Element(required = false)
    @Convert(UtcDateConverter.class)
    Date createDate;

    @Nullable
    @Element(required = false)
    String surveyTag;

    @Nullable
    @Element(required = false)
    @Convert(UtcDateConverter.class)
    Date completeDate;

    @Nullable
    @Element(required = false, name = "schoolNo")
    String schoolId;

    @Nullable
    @Element(required = false)
    String schoolName;

    @ElementList(inline = true)
    List<SerializableGroup> groups;

    @Nullable
    @Element(required = false, name = "surveyCompleted")
    @Convert(SurveyStateConverter.class)
    SurveyState state;

    @Nullable
    @Element(required = false)
    String createUser;

    @Nullable
    @Element(required = false)
    String lastEditedUser;

    public SerializableWashSurvey(WashSurvey other) {
        this.type = other.getSurveyType();
        this.region = other.getAppRegion();
        this.version = other.getVersion();
        this.createDate = other.getCreateDate();
        this.surveyTag = other.getSurveyTag();
        this.completeDate = other.getCompleteDate();
        this.schoolId = other.getSchoolId();
        this.schoolName = other.getSchoolName();
        this.state = ObjectUtils.orElse(other.getState(), SurveyState.NOT_COMPLETED);
        this.createUser = other.getCreateUser();
        this.lastEditedUser = other.getLastEditedUser();

        if (other.getGroups() != null) {
            this.groups = other.getGroups().stream().map(SerializableGroup::new).collect(Collectors.toList());
        }
    }

    public SerializableWashSurvey() {
        // required for serialization
    }

    @Nullable
    @Override
    public List<? extends Group> getGroups() {
        return groups;
    }

    @Override
    public int getVersion() {
        return version;
    }

    @NonNull
    @Override
    public SurveyType getSurveyType() {
        return type;
    }

    @Nullable
    @Override
    public Date getCreateDate() {
        return createDate;
    }

    @Nullable
    @Override
    public String getSurveyTag() {
        return surveyTag;
    }

    @Nullable
    @Override
    public Date getCompleteDate() {
        return completeDate;
    }

    @Nullable
    @Override
    public String getSchoolName() {
        return schoolName;
    }

    @Nullable
    @Override
    public String getSchoolId() {
        return schoolId;
    }

    @NonNull
    @Override
    public AppRegion getAppRegion() {
        return region;
    }

    @NonNull
    @Override
    public Progress getProgress() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SurveyState getState() {
        return state;
    }

    @Nullable
    @Override
    public String getCreateUser() {
        return createUser;
    }

    @Nullable
    @Override
    public String getLastEditedUser() {
        return lastEditedUser;
    }
}
