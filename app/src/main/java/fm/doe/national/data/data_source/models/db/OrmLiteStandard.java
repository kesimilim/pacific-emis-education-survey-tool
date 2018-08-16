package fm.doe.national.data.data_source.models.db;

import android.support.annotation.NonNull;

import java.util.Collection;

import fm.doe.national.data.data_source.models.Criteria;
import fm.doe.national.data.data_source.models.GroupStandard;
import fm.doe.national.data.data_source.models.Standard;

public class OrmLiteStandard implements Standard {

    private OrmLiteSurveyItem surveyItem;

    public OrmLiteStandard(OrmLiteSurveyItem surveyItem) {
        this.surveyItem = surveyItem;
    }

    @Override
    public GroupStandard getGroupStandard() {
        return new OrmLiteGroupStandard(surveyItem.getParentItem());
    }

    @NonNull
    @Override
    public String getName() {
        return surveyItem.getName();
    }

    @Override
    public Collection<? extends Criteria> getCriterias() {
        return null;
    }

}

