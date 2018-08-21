package fm.doe.national.data.data_source.models.serializable;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.PropertyElement;
import com.tickaroo.tikxml.annotation.Xml;

import java.util.Collection;
import java.util.List;

import fm.doe.national.data.data_source.models.Criteria;
import fm.doe.national.data.data_source.models.Standard;
import fm.doe.national.data.data_source.models.SubCriteria;

@Xml(name = "criteria")
public class SerializableCriteria implements Criteria {

    @PropertyElement
    private String name;

    @Element
    List<SerializableSubCriteria> subCriterias;

    @Nullable
    @Override
    public Standard getStandard() {
        return null;
    }

    @NonNull
    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<? extends SubCriteria> getSubCriterias() {
        return subCriterias;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSubCriterias(List<SerializableSubCriteria> subCriterias) {
        this.subCriterias = subCriterias;
    }
}
