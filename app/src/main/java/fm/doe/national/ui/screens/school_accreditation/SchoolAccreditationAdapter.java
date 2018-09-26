package fm.doe.national.ui.screens.school_accreditation;

import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.data.data_source.models.School;
import fm.doe.national.data.data_source.models.SchoolAccreditationPassing;
import fm.doe.national.ui.screens.base.BaseAdapter;
import fm.doe.national.utils.DateUtils;
import fm.doe.national.utils.ViewUtils;

public class SchoolAccreditationAdapter extends BaseAdapter<SchoolAccreditationPassing> {

    @Override
    protected SchoolAccreditationViewHolder provideViewHolder(ViewGroup parent) {
        return new SchoolAccreditationViewHolder(parent);
    }

    protected class SchoolAccreditationViewHolder extends ViewHolder {

        @BindView(R.id.textview_id_school)
        TextView schoolIdTextView;

        @BindView(R.id.textview_name_school)
        TextView nameSchoolTextView;

        @BindView(R.id.textview_progress)
        TextView progressTextView;

        @BindView(R.id.textview_created_year)
        TextView createdYearTextView;

        @BindView(R.id.progressbar)
        ProgressBar progressBar;

        SchoolAccreditationViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_survey);
        }

        @Override
        public void onBind(SchoolAccreditationPassing item) {
            School school = item.getSchool();
            schoolIdTextView.setText(school.getId());
            nameSchoolTextView.setText(school.getName());
            createdYearTextView.setText(DateUtils.formatFromYearToMonth(item.getStartDate()));

            itemView.setOnClickListener(this);

            ViewUtils.rebindProgress(
                    item.getSchoolAccreditation().getCategoryProgress().getTotalQuestionsCount(),
                    item.getSchoolAccreditation().getCategoryProgress().getAnsweredQuestionsCount(),
                    getString(R.string.criteria_progress),
                    progressTextView, progressBar);
        }

    }
}