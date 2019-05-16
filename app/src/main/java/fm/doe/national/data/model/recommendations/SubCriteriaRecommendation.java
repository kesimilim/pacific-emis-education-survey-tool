package fm.doe.national.data.model.recommendations;

import fm.doe.national.data.model.SubCriteria;

public class SubCriteriaRecommendation extends Recommendation<SubCriteria> {

    private static final int LEVEL = 3;

    public SubCriteriaRecommendation(SubCriteria object) {
        super(object, object.getSuffix() + " " + object.getTitle(), LEVEL);
    }
}
