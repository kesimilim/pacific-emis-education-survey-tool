package fm.doe.national.wash_core.data.model;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import java.util.List;

import fm.doe.national.core.data.model.IdentifiedObject;
import fm.doe.national.core.data.model.Photo;
import fm.doe.national.core.utils.CollectionUtils;

public interface Answer extends IdentifiedObject {

    @Nullable
    List<String> getItems();

    @Nullable
    String getComment();

    @Nullable
    String getInputText();

    @Nullable
    List<Variant> getVariants();

    @Nullable
    Location getLocation();

    @Nullable
    List<? extends Photo> getPhotos();

    @Nullable
    BinaryAnswerState getBinaryAnswerState();

    @Nullable
    TernaryAnswerState getTernaryAnswerState();

    default boolean isAnsweredForQuestionType(QuestionType questionType) {
        switch (questionType) {
            case BINARY:
                return getBinaryAnswerState() != null;
            case TERNARY:
                return getTernaryAnswerState() != null;
            case GEOLOCATION:
                return getLocation() != null;
            case PHOTO:
                return !CollectionUtils.isEmpty(getPhotos());
            case TEXT_INPUT:
            case NUMBER_INPUT:
            case PHONE_INPUT:
                return !TextUtils.isEmpty(getInputText());
            case SINGLE_SELECTION:
            case MULTI_SELECTION:
                return !CollectionUtils.isEmpty(getItems());
            case COMPLEX_BINARY:
            case COMPLEX_NUMBER_INPUT:
                return !CollectionUtils.isEmpty(getVariants());
        }
        throw new IllegalStateException();
    }

}
