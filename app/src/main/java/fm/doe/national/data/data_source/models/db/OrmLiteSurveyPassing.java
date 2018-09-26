package fm.doe.national.data.data_source.models.db;

import com.j256.ormlite.field.DatabaseField;

import java.util.Date;

public class OrmLiteSurveyPassing {

    public interface Column {
        String ID = "id";
        String SCHOOL = "school";
        String SURVEY = "surveyPassing";
        String START_DATE = "startDate";
        String END_DATE = "endDate";
    }

    @DatabaseField(generatedId = true, columnName = Column.ID)
    protected long id;

    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true, columnName = Column.SCHOOL)
    protected OrmLiteSchool school;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true, columnName = Column.SURVEY)
    protected OrmLiteSurvey survey;

    @DatabaseField(columnName = Column.START_DATE)
    protected Date startDate;

    @DatabaseField(columnName = Column.END_DATE)
    protected Date endDate;

    public OrmLiteSurveyPassing() {
    }

    public OrmLiteSurveyPassing(Date startDate, OrmLiteSchool school, OrmLiteSurvey survey) {
        this.school = school;
        this.survey = survey;
        this.startDate = startDate;
    }

    public long getId() {
        return id;
    }

    public OrmLiteSchool getSchool() {
        return school;
    }

    public void setSchool(OrmLiteSchool school) {
        this.school = school;
    }

    public OrmLiteSurvey getSurvey() {
        return survey;
    }

    public void setSurvey(OrmLiteSurvey survey) {
        this.survey = survey;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
