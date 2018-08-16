package fm.doe.national.data.data_source.models.db;

import android.support.annotation.NonNull;

import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fm.doe.national.data.data_source.models.Criteria;
import fm.doe.national.data.data_source.models.Standard;
import fm.doe.national.data.data_source.models.SubCriteria;

@DatabaseTable
public class OrmLiteCriteria implements Criteria{

    private OrmLiteSurveyItem surveyItem;

    public OrmLiteCriteria(OrmLiteSurveyItem surveyItem) {
        this.surveyItem = surveyItem;
    }

    @Override
    public Standard getStandard() {
        return new OrmLiteStandard(surveyItem.getParentItem());
    }

    @NonNull
    @Override
    public String getName() {
        return surveyItem.getName();
    }

    @Override
    public Collection<? extends SubCriteria> getSubCriterias() {
        List<SubCriteria> subCriteriaList = new ArrayList<>();
        for (OrmLiteSurveyItem surveyItem : surveyItem.getChildrenItems()) {
            subCriteriaList.add(new OrmLiteSubCriteria(surveyItem));
        }
        return subCriteriaList;
    }

}
