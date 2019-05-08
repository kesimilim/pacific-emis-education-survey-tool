package fm.doe.national.ui.screens.criterias;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;
import fm.doe.national.R;
import fm.doe.national.data.model.Answer;
import fm.doe.national.data.model.AnswerState;
import fm.doe.national.data.model.Photo;
import fm.doe.national.data.model.mutable.MutableAnswer;
import fm.doe.national.data.model.mutable.MutableSubCriteria;
import fm.doe.national.ui.custom_views.SwitchableButton;
import fm.doe.national.ui.custom_views.photos_view.PhotoBoxView;
import fm.doe.national.ui.screens.base.BaseAdapter;
import io.reactivex.Observable;

public class SubCriteriaListAdapter extends BaseAdapter<MutableSubCriteria> {

    @Nullable
    private SubcriteriaCallback callback = null;

    public void setCallback(@Nullable SubcriteriaCallback callback) {
        this.callback = callback;
    }

    @Nullable
    private OnAnswerStateChangedListener answerStateChangedListener = null;

    public void setAnswerStateChangedListener(@Nullable OnAnswerStateChangedListener answerStateChangedListener) {
        this.answerStateChangedListener = answerStateChangedListener;
    }

    @Override
    protected SubCriteriaViewHolder provideViewHolder(ViewGroup parent) {
        return new SubCriteriaViewHolder(parent);
    }

    class SubCriteriaViewHolder extends ViewHolder implements
            SwitchableButton.StateChangedListener,
            PhotoBoxView.Callback {

        private final View popupView;
        private final TextView hintView;

        @BindView(R.id.textview_alphabetical_numbering)
        TextView numberingTextView;

        @BindView(R.id.textview_question)
        TextView questionTextView;

        @BindView(R.id.switchablebutton_answer)
        SwitchableButton switchableButton;

        @BindView(R.id.textview_interview_questions)
        TextView interviewQuestionsTextView;

        @BindView(R.id.imageview_comment_button)
        View commentButtonView;

        @BindView(R.id.imageview_photo_button)
        View photoButtonView;

        @BindView(R.id.layout_comment)
        View commentView;

        @BindView(R.id.imagebutton_delete)
        View commentDeleteButton;

        @BindView(R.id.imageview_edit_button)
        View commentEditButton;

        @BindView(R.id.textview_comment)
        TextView commentTextView;

        @BindView(R.id.photoboxview)
        PhotoBoxView photoBoxView;

        SubCriteriaViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_sub_criteria);
            switchableButton.setListener(this);
            popupView = LayoutInflater.from(parent.getContext()).inflate(R.layout.popup_hint, parent, false);
            hintView = popupView.findViewById(R.id.textview_hint);
            photoBoxView.setCallback(this);
        }

        @Override
        public void onBind(MutableSubCriteria item) {
            Answer answer = item.getAnswer();
            questionTextView.setText(item.getTitle());

            numberingTextView.setText(getString(R.string.format_subcriteria, item.getSuffix()));

            if (!TextUtils.isEmpty(item.getInterviewQuestions())) {
                interviewQuestionsTextView.setVisibility(View.VISIBLE);
                interviewQuestionsTextView.setText(item.getInterviewQuestions());
            } else {
                interviewQuestionsTextView.setVisibility(View.GONE);
            }
            switchableButton.setStateNotNotifying(convertToUiState(answer.getState()));

            updateCommentVisibility(answer.getComment());
            updatePhotosVisibility(answer.getPhotos());
        }

        @OnLongClick(R.id.textview_question)
        public boolean onViewLongClick(View v) {
            if (!TextUtils.isEmpty(getItem().getHint())) showHint();
            return true;
        }

        @OnClick({
                R.id.imageview_comment_button,
                R.id.imageview_photo_button,
                R.id.imagebutton_delete,
                R.id.imageview_edit_button,
                R.id.imagebutton_add_photo
        })
        public void onViewClick(View v) {
            if (callback != null) {
                MutableSubCriteria item = getItem();
                switch (v.getId()) {
                    case R.id.imageview_comment_button:
                        callback.onAddCommentClicked(item);
                        break;
                    case R.id.imageview_photo_button:
                        callback.onAddPhotoClicked(item);
                        break;
                    case R.id.imagebutton_delete:
                        callback.onRemoveCommentClicked(item);
                        break;
                    case R.id.imageview_edit_button:
                        callback.onEditCommentClicked(item);
                        break;
                    case R.id.imagebutton_add_photo:
                        callback.onAddPhotoClicked(item);
                        break;
                }
            }

        }

        @Override
        public void onStateChanged(SwitchableButton view, SwitchableButton.State state) {
            MutableAnswer answer = getItem().getAnswer();
            answer.setState(convertFromUiState(state));
            notifyStateChanged();
        }

        @Override
        public void onAddPhotoClick() {
            if (callback != null) callback.onAddPhotoClicked(getItem());
        }

        @Override
        public void onMorePhotosClick() {
            if (callback != null) callback.onMorePhotosClick(getItem());
        }

        @Override
        public void onDeletePhotoClick(String photo) {
            if (callback != null) callback.onRemovePhotoClicked(getItem(), photo);
        }

        @Override
        public void onPhotoClick(View v, String photo) {
            if (callback != null) callback.onPhotoClicked(v, photo);
        }

        private SwitchableButton.State convertToUiState(AnswerState state) {
            switch (state) {
                case NOT_ANSWERED:
                    return SwitchableButton.State.NEUTRAL;
                case NEGATIVE:
                    return SwitchableButton.State.NEGATIVE;
                case POSITIVE:
                    return SwitchableButton.State.POSITIVE;
            }
            return SwitchableButton.State.NEUTRAL; // unreachable code
        }

        private AnswerState convertFromUiState(SwitchableButton.State state) {
            switch (state) {
                case NEUTRAL:
                    return AnswerState.NOT_ANSWERED;
                case NEGATIVE:
                    return AnswerState.NEGATIVE;
                case POSITIVE:
                    return AnswerState.POSITIVE;
            }
            return AnswerState.NOT_ANSWERED; // unreachable code
        }

        private void notifyStateChanged() {
            MutableSubCriteria subCriteria = getItem();
            if (callback != null) callback.onSubCriteriaStateChanged(subCriteria);
            if (answerStateChangedListener != null)
                answerStateChangedListener.onSubCriteriaAnswerChanged(subCriteria);
        }

        private void showHint() {
            hintView.setText(getItem().getHint());
            PopupWindow popupWindow = new PopupWindow(
                    popupView,
                    itemView.getMeasuredWidth(),
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.setOutsideTouchable(true);

            // fix - on devices with Android ver. < 6.0 it's impossible to close popup window
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                popupWindow.setFocusable(true);
                popupWindow.setBackgroundDrawable(new ColorDrawable());
            }

            // need to measure view before it rendered
            // showAsDropDown cannot draw popup above anchor in ViewHolder
            // so just offset it manually
            popupView.measure(View.MeasureSpec.makeMeasureSpec(itemView.getMeasuredWidth(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

            popupWindow.showAsDropDown(itemView,
                    0,
                    -itemView.getMeasuredHeight() - popupView.getMeasuredHeight(),
                    Gravity.TOP);
        }

        private void updateCommentVisibility(@Nullable String currentComment) {
            if (TextUtils.isEmpty(currentComment)) {
                commentView.setVisibility(View.GONE);
                commentButtonView.setVisibility(View.VISIBLE);
            } else {
                commentView.setVisibility(View.VISIBLE);
                commentButtonView.setVisibility(View.GONE);
                commentTextView.setText(currentComment);
            }
        }

        private void updatePhotosVisibility(List<? extends Photo> photos) {
            if (photos.isEmpty()) {
                photoBoxView.setVisibility(View.GONE);
                photoButtonView.setVisibility(View.VISIBLE);
            } else {
                photoBoxView.setVisibility(View.VISIBLE);
                photoButtonView.setVisibility(View.GONE);
                List<String> localPaths = Observable.fromIterable(photos)
                        .map(Photo::getLocalPath)
                        .toList()
                        .blockingGet();
                photoBoxView.setPhotos(localPaths);
            }
        }
    }

    public interface OnAnswerStateChangedListener {
        void onSubCriteriaAnswerChanged(@NonNull MutableSubCriteria subCriteria);
    }
}
