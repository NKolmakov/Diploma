var questNumber = 0;
var questHeaderNumber = 1;
var questionPlaceholder = document.getElementById("form.placeholder.question").value;
var answerPlaceholder = document.getElementById("form.placeholder.answer").value;
var questionNumber = document.getElementById("form.label.questionNumber").value;
var addAnswer = document.getElementById("form.button.addAnswer").value;

$(function (){
    $(".createQuestion").click(function (){
        var html2Add =
        "<div id='question#"+ ++questNumber +"' class='question'>"+
            "<input hidden id='lastAnsIndex#"+questNumber+"' value='1'>"+
            "<h4>"+questionNumber+ ++questHeaderNumber +"</h4>"+
            "<button class='removeQuestion' type='button'> - </button><br>"+
            "<textarea name='questions["+questNumber+"].name' placeholder='"+questionPlaceholder+"' maxlength='255' required></textarea>"+
            "<button class='createAnswer' type='button'>"+addAnswer+"</button>"+
            "<div id='answers#"+questNumber+"' class='answers'>"+
                "<div id='answer#0'>"+
                    "<input name='questions["+questNumber+"].answers[0].right' type='checkbox'>"+
                    "<input name='questions["+questNumber+"].answers[0].name' type='text' placeholder='"+answerPlaceholder+"' maxlength='255' required><br>"+
                "</div>"+
            "</div>"+
        "</div>";
        $(".questionBlock").prepend(html2Add);
    })
});

$(function (){
    $(".questionBlock").on("click",".createAnswer",function (){
        var currentQuestionNumber = $(this).parent().attr("id").replace("question#","");
        var currentAnswer = $(this).parent().find("div[id^='answers#']");
        var lastAnswer = document.getElementById("lastAnsIndex#"+currentQuestionNumber);
        lastIndex = lastAnswer.value;

        var html2Add =
        "<div id='answer#"+lastIndex+"'>"+
             "<input name='questions["+questNumber+"].answers["+lastIndex+"].right' type='checkbox'>"+
             "<input name='questions["+questNumber+"].answers["+lastIndex+"].name' type='text' placeholder='"+answerPlaceholder+"' maxlength='255' required>"+
             "<button class='removeAnswer' type='button'> - </button><br>"+
         "</div>";
        $(currentAnswer).append(html2Add);
        $(lastAnswer).val(++lastIndex);
    })
});

$(function () {
    $(".questionBlock").on("click",".removeAnswer",function (){
        var currentQuestionNumber = $(this).parent().parent().parent().attr("id").replace("question#","");
        var lastAnswer = document.getElementById("lastAnsIndex#"+currentQuestionNumber);
        lastIndex = lastAnswer.value;

        var currentAnswer = $(this).parent();
        $(currentAnswer).remove();
        $(lastAnswer).val(--lastIndex);
        updateQuestions();
    })
});

$(function (){
    $(".questionBlock").on("click",".removeQuestion",function(){
        var currentQuestion = $(this).parent();
        $(currentQuestion).remove();

        var parentObject = document.getElementsByClassName("questionBlock")[0];
        var nestedObjects = parentObject.getElementsByTagName("H4");

        for(var i=0; i < nestedObjects.length; i++){
            nestedObjects[i].innerHTML = questionNumber+(nestedObjects.length-i);
        }

        questHeaderNumber = nestedObjects.length;
        updateQuestions();
    })
});

function updateQuestions(){
    var parentObject = document.getElementsByClassName("questionBlock")[0];
    var questions = $(parentObject).find("div[id^='question#']");

    for(var i=0; i<questions.length; i++){
        questions[i].id = "question#"+(questions.length-i-1);
        var answers = $(questions[i]).find("div[id^='answers#']");
        for(var j = 0; j < answers.length; j++){
            answers[j].id = "answers#"+(questions.length-i-1);
            var childAnswers = $(answers[j]).find("div[id^='answer#']");

            for(var k = 0; k < childAnswers.length; k++){
                childAnswers[k].id = "answer#"+k;
                var inputCheckbox = $(childAnswers[k]).find("input:checkbox");
                var inputText = $(childAnswers[k]).find("input:text");

                inputCheckbox.name = "questions["+i+"].answers["+k+"].right";
                inputText.name = "questions["+i+"].answers["+k+"].name";
            }
        }
    }

    questNumber = questions.length-1;
}

