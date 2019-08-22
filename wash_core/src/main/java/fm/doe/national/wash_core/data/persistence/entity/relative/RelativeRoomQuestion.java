package fm.doe.national.wash_core.data.persistence.entity.relative;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import fm.doe.national.core.utils.CollectionUtils;
import fm.doe.national.wash_core.data.model.mutable.MutableQuestion;
import fm.doe.national.wash_core.data.persistence.entity.RoomAnswer;
import fm.doe.national.wash_core.data.persistence.entity.RoomQuestion;

public class RelativeRoomQuestion {

    @Embedded
    public RoomQuestion question;

    @Relation(parentColumn = "uid", entityColumn = "question_id", entity = RoomAnswer.class)
    public List<RelativeRoomAnswer> answers;

    public MutableQuestion toMutable() {
        MutableQuestion question = new MutableQuestion(this.question);
        if (!CollectionUtils.isEmpty(answers)) {
            question.setAnswer(answers.get(0).toMutable());
        }
        return question;
    }

}
