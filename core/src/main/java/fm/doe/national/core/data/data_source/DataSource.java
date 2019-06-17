package fm.doe.national.core.data.data_source;

import java.util.Date;
import java.util.List;

import fm.doe.national.core.data.model.Photo;
import fm.doe.national.core.data.model.School;
import fm.doe.national.core.data.model.Survey;
import io.reactivex.Completable;
import io.reactivex.Single;

public interface DataSource {

    Single<List<School>> loadSchools();

    Completable rewriteAllSchools(List<School> schools);

    Completable rewriteTemplateSurvey(Survey survey);

    Single<Survey> getTemplateSurvey();

    Single<Survey> loadSurvey(long surveyId);

    Single<List<Survey>> loadAllSurveys();

    Single<Survey> createSurvey(String schoolId, String schoolName, Date date);

    Completable deleteSurvey(long surveyId);

    Single<Photo> createPhoto(Photo photo, long answerId);

    Completable deletePhoto(long photoId);

    Completable deleteCreatedSurveys();

    Completable createPartiallySavedSurvey(Survey survey);

}