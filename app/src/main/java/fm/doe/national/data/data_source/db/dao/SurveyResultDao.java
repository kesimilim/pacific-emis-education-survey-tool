package fm.doe.national.data.data_source.db.dao;

import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.Date;

import fm.doe.national.data.data_source.models.db.OrmLiteSchool;
import fm.doe.national.data.data_source.models.db.OrmLiteBaseSurveyResult;
import io.reactivex.Single;

public class SurveyResultDao extends BaseRxDao<OrmLiteBaseSurveyResult, Long> {

    private SurveyDao surveyDao;

    SurveyResultDao(SurveyDao surveyDao, ConnectionSource connectionSource, Class<OrmLiteBaseSurveyResult> dataClass) throws SQLException {
        super(connectionSource, dataClass);
        this.surveyDao = surveyDao;
    }

    public Single<OrmLiteBaseSurveyResult> createSurveyResult(int year, OrmLiteSchool school) throws SQLException {
        return Single
                .just(new OrmLiteBaseSurveyResult(year, school, surveyDao.getRelevantSurvey()))
                .map(this::createIfNotExists);
    }

    public Single<OrmLiteBaseSurveyResult> requestSurveyResult(Date startDate) {
        return Single.fromCallable(() -> queryBuilder()
                        .where()
                        .eq(OrmLiteBaseSurveyResult.Column.START_DATE, startDate).queryForFirst());
    }

}
