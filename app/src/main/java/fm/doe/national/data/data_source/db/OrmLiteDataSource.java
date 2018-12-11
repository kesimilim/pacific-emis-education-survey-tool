package fm.doe.national.data.data_source.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fm.doe.national.data.data_source.DataSource;
import fm.doe.national.data.data_source.db.dao.AnswerDao;
import fm.doe.national.data.data_source.db.dao.CategoryProgressDao;
import fm.doe.national.data.data_source.db.dao.DatabaseHelper;
import fm.doe.national.data.data_source.db.dao.SchoolDao;
import fm.doe.national.data.data_source.db.dao.SubcriteriaQuestionDao;
import fm.doe.national.data.data_source.db.dao.SurveyDao;
import fm.doe.national.data.data_source.db.dao.SurveyItemDao;
import fm.doe.national.data.data_source.db.dao.SurveyPassingDao;
import fm.doe.national.data.data_source.models.Answer;
import fm.doe.national.data.data_source.models.Category;
import fm.doe.national.data.data_source.models.Criteria;
import fm.doe.national.data.data_source.models.School;
import fm.doe.national.data.data_source.models.SchoolAccreditationPassing;
import fm.doe.national.data.data_source.models.Standard;
import fm.doe.national.data.data_source.models.db.wrappers.OrmLiteCriteria;
import fm.doe.national.data.data_source.models.db.wrappers.OrmLiteCategory;
import fm.doe.national.data.data_source.models.db.wrappers.OrmLiteSchoolAccreditation;
import fm.doe.national.data.data_source.models.db.wrappers.OrmLiteSchoolAccreditationPassing;
import fm.doe.national.data.data_source.models.db.wrappers.OrmLiteStandard;
import fm.doe.national.data.data_source.models.db.wrappers.OrmLiteSubCriteria;
import fm.doe.national.data.data_source.models.serializable.LinkedCategory;
import fm.doe.national.data.data_source.models.serializable.LinkedSchoolAccreditation;
import fm.doe.national.data.data_source.models.serializable.LinkedStandard;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public class OrmLiteDataSource implements DataSource {

    private SchoolDao schoolDao;
    private SurveyDao surveyDao;
    private SurveyItemDao surveyItemDao;
    private SurveyPassingDao surveyPassingDao;
    private CategoryProgressDao categoryProgressDao;
    private AnswerDao answerDao;
    private SubcriteriaQuestionDao subcriteriaQuestionDao;

    public OrmLiteDataSource(DatabaseHelper helper) {
        try {
            schoolDao = helper.getSchoolDao();
            surveyDao = helper.getSurveyDao();
            surveyItemDao = helper.getSurveyItemDao();
            answerDao = helper.getAnswerDao();
            surveyPassingDao = helper.getSurveyPassingDao();
            categoryProgressDao = helper.getCategoryProgressDao();
            subcriteriaQuestionDao = helper.getSubcriteriaQuestionDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Completable updateSchools(List<School> schoolList) {
        return schoolDao.updateSchools(schoolList);
    }


    @Override
    public Single<List<School>> requestSchools() {
        return schoolDao.getAllQueriesSingle()
                .map(ArrayList<School>::new);
    }

    @Override
    public Single<Answer> updateAnswer(long passingId,
                                    long subCriteriaId,
                                    Answer answer) {
        return surveyPassingDao.getItemSingle(passingId)
                .flatMap(passing -> surveyItemDao.getItemSingle(subCriteriaId)
                        .flatMap(subCriteriaItem -> answerDao.requestAnswer(subCriteriaItem, passing)
                                .flatMap(oldAnswer -> Single.zip(
                                        categoryProgressDao.updateCategoryProgress(
                                                subCriteriaItem.getParentItem(),
                                                passing,
                                                oldAnswer.getState(),
                                                answer.getState()),
                                        answerDao.updateAnswer(subCriteriaItem,
                                                passing,
                                                answer.getState(),
                                                answer.getComment(),
                                                answer.getPhotos()),
                                        (progress, updatedAnswer) -> updatedAnswer)
                                )));
    }


    @Override
    public Completable createSchoolAccreditation(LinkedSchoolAccreditation schoolAccreditation) {
        return surveyDao.createSchoolAccreditation(
                schoolAccreditation.getVersion(),
                schoolAccreditation.getType(),
                schoolAccreditation.getCategories());
    }

    @Override
    public Single<SchoolAccreditationPassing> createNewSchoolAccreditationPassing(Date startDate, School school) {
        return schoolDao.requestSchool(school.getId())
                .flatMap(schoolItem -> surveyPassingDao.createSurveyPassing(startDate, schoolItem)
                        .flatMap(surveyPassing -> categoryProgressDao.requestCategoryProgress(
                                surveyPassing,
                                surveyPassing.getSurvey().getSurveyItems())
                                .map(progress -> new OrmLiteSchoolAccreditation(surveyPassing.getSurvey(), progress))
                                .map(schoolAccreditation -> new OrmLiteSchoolAccreditationPassing(
                                        surveyPassing,
                                        schoolAccreditation))));
    }

    @Override
    public Single<List<SchoolAccreditationPassing>> requestSchoolAccreditationPassings() {
        return surveyPassingDao.getAllQueriesSingle()
                .toObservable()
                .flatMapIterable(resultList -> resultList)
                .flatMap(surveyPassing -> requestSchoolAccreditationPassing(surveyPassing.getId())
                        .toObservable())
                .toList();
    }

    @Override
    public Completable removeSchoolAccreditationPassing(long passingId) {
        return Completable.fromAction(() -> surveyPassingDao.deleteById(passingId));
    }

    @Override
    public Single<SchoolAccreditationPassing> requestSchoolAccreditationPassing(long passingId) {
        return surveyPassingDao.getItemSingle(passingId)
                .flatMap(surveyPassing -> categoryProgressDao.requestCategoryProgress(
                        surveyPassing, surveyPassing.getSurvey().getSurveyItems())
                        .map(progress -> new OrmLiteSchoolAccreditationPassing(
                                surveyPassing,
                                new OrmLiteSchoolAccreditation(surveyPassing.getSurvey(), progress))));
    }

    @Override
    public Single<List<Category>> requestCategories(long passingId) {
        return surveyPassingDao.getItemSingle(passingId)
                .flatMap(passing -> Observable.fromIterable(passing.getSurvey().getSurveyItems())
                        .flatMap(surveyItem -> categoryProgressDao.requestCategoryProgress(passing, surveyItem)
                                .map(progress -> new OrmLiteCategory(surveyItem, progress))
                                .toObservable())
                        .toList()
                        .map(ArrayList<Category>::new));
    }

    @Override
    public Single<Standard> requestStandard(long passingId, long standardId) {
        return surveyPassingDao.getItemSingle(passingId)
                .flatMap(passing -> surveyItemDao.getItemSingle(standardId)
                        .flatMap(standardItem -> categoryProgressDao.requestCategoryProgress(passing, standardItem)
                                .map(progress -> new OrmLiteStandard(standardItem, progress))
                        ));
    }

    @Override
    public Single<List<Standard>> requestStandards(long passingId) {
        return requestCategories(passingId)
                .flatMap(groupStandards -> Observable.fromIterable(groupStandards)
                        .concatMap(groupStandard -> requestStandards(passingId, groupStandard.getId())
                                .toObservable())
                        .reduce((accumulator, current) -> {
                            accumulator.addAll(current);
                            return accumulator;
                        })
                        .toSingle());
    }

    @Override
    public Single<List<Standard>> requestStandards(long passingId, long categoryId) {
        return surveyPassingDao.getItemSingle(passingId)
                .flatMap(passing -> surveyItemDao.getItemSingle(categoryId)
                        .flatMap(groupStandardItem -> Observable.fromIterable(groupStandardItem.getChildrenItems())
                                .flatMap(standardItem -> categoryProgressDao.requestCategoryProgress(passing, standardItem)
                                        .map(progress -> new OrmLiteStandard(standardItem, progress))
                                        .toObservable())
                                .toList()
                                .map(ArrayList<Standard>::new)));
    }

    @Override
    public Single<LinkedSchoolAccreditation> requestLinkedSchoolAccreditation(long passingId) {
        return surveyPassingDao.getItemSingle(passingId)
                .flatMap(surveyPassing -> Single.zip(
                        categoryProgressDao.requestCategoryProgress(surveyPassing, surveyPassing.getSurvey().getSurveyItems()),
                        requestLinkedGroupStandards(passingId),
                        (progress, groupStandards) -> new OrmLiteSchoolAccreditation(
                                surveyPassing.getSurvey(), progress, groupStandards)
                ));
    }

    private Single<List<LinkedCategory>> requestLinkedGroupStandards(long passingId) {
        return surveyPassingDao.getItemSingle(passingId)
                .flatMap(passing -> Observable.fromIterable(passing.getSurvey().getSurveyItems())
                        .flatMap(surveyItem -> Single.zip(
                                categoryProgressDao.requestCategoryProgress(passing, surveyItem),
                                requestLinkedStandards(passingId, surveyItem.getId()),
                                (progress, standards) -> new OrmLiteCategory(surveyItem, progress, standards))
                                .toObservable())
                        .toList()
                        .map(ArrayList<LinkedCategory>::new));
    }

    private Single<List<LinkedStandard>> requestLinkedStandards(long passingId, long groupStandardId) {
        return surveyPassingDao.getItemSingle(passingId)
                .flatMap(passing -> surveyItemDao.getItemSingle(groupStandardId)
                        .flatMap(groupStandardItem -> Observable.fromIterable(groupStandardItem.getChildrenItems())
                                .flatMap(standardItem -> Single.zip(
                                        categoryProgressDao.requestCategoryProgress(passing, standardItem),
                                        requestCriterias(passingId, standardItem.getId()),
                                        (progress, criterias) -> new OrmLiteStandard(standardItem, progress, criterias))
                                        .toObservable())
                                .toList()
                                .map(ArrayList<LinkedStandard>::new)));
    }

    @Override
    public Single<List<Criteria>> requestCriterias(long passingId, long standardId) {
        return surveyPassingDao.getItemSingle(passingId)
                .flatMap(passing -> surveyItemDao.getItemSingle(standardId)
                        .flatMap(standardItem -> Observable.fromIterable(standardItem.getChildrenItems())
                                .flatMap(criteriaItem -> Observable.zip(
                                        categoryProgressDao.requestCategoryProgress(passing, criteriaItem)
                                                .toObservable(),
                                        Observable.fromIterable(criteriaItem.getChildrenItems())
                                                .flatMap(subcriteriaItem -> answerDao.requestAnswer(subcriteriaItem, passing)
                                                        .flatMap(answer -> subcriteriaQuestionDao.requestQuestion(subcriteriaItem)
                                                            .map(question -> new OrmLiteSubCriteria(subcriteriaItem, answer, question)))
                                                        .toObservable())
                                                .toList()
                                                .toObservable(),
                                        (progress, subCriterias) -> new OrmLiteCriteria(criteriaItem, subCriterias, progress)))
                                .toList())
                        .map(ArrayList<Criteria>::new));
    }

    @Override
    public Single<Category> requestCategoryOfStandard(long passingId, Standard standard) {
        return surveyPassingDao.getItemSingle(passingId)
                .flatMap(passing -> surveyItemDao.getItemSingle(standard.getId())
                        .flatMap(standardItem -> categoryProgressDao.requestCategoryProgress(passing, standardItem.getParentItem())
                                .map(progress -> new OrmLiteCategory(standardItem.getParentItem(), progress))));
    }

    @Override
    public Single<Answer> requestAnswer(long passingId, long subCriteriaId) {
        return surveyPassingDao.getItemSingle(passingId)
                .flatMap(passing -> surveyItemDao.getItemSingle(subCriteriaId)
                                .flatMap(subCriteria -> answerDao.requestAnswer(subCriteria, passing)));
    }
}
