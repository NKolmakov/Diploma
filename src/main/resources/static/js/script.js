var questNumber = 0;
// on window load remove comments
$('*').contents().each(function() {
     if(this.nodeType === Node.COMMENT_NODE) {
       $(this).remove();
     }
});

//radios on form have different names but referred to one group
//for this purpose if radio clicked on one answer then turn off others in current question
$(function(){
    $(".question").on("click","input:radio",function(){
        var currentQuestion = $(this).closest(".question")[0];
        var currentRadio = $(this);
        var radios = $(currentQuestion).find("input:radio");
        
        for(var i = 0; i < radios.length; i++){
            $(radios[i]).not(currentRadio).prop('checked', false);
        }

    })
});

$(function(){
    $("#passTest").ready(function(){
        var startTime = getCurrentTime();
        $("#passTest").append("<input hidden id='startTime' name='startTime' value='"+startTime+"'>");
    })
});
//timer function starts when available element with class='timer'
    var initialTime = $('#initTime').val();
    var endOfTime = new Date().getTime() + parseInt(initialTime*60*1000);
    if(initialTime != null){
        var timing = setInterval(function () {

            var currentDate = new Date().getTime();
            var timeLeft = endOfTime - currentDate;

            var hours = Math.floor((timeLeft % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
            if (hours < 10) hours="0"+hours;
            var minutes = Math.floor((timeLeft % (1000 * 60 * 60)) / (1000 * 60));
            if (minutes < 10) minutes="0"+minutes;
            var seconds = Math.floor((timeLeft % (1000 * 60)) / 1000);
            if (seconds < 10) seconds="0"+seconds;

            var time = "";

            if(hours !== "00") time+=hours+"h "
            if(minutes !== "00") time+=minutes+"m "
            document.getElementById("time").innerHTML = time + seconds + "s";

            if (timeLeft <= 0) {
              var dt = new Date();
              var endTime = getCurrentTime();
              clearInterval(timing);
              alert("Time is over. Test will be send automatically");
              $("#passTest").append('<input hidden name="endTime" value="'+endTime+'">');
              $('#passTest').submit();
            }
        }, 1000);
    }

function getCurrentTime(){
    var dt = new Date();
    var hours = dt.getHours();
    var minutes = dt.getMinutes();
    var seconds = dt.getSeconds();

    if(hours<10) hours = "0"+hours;
    if(minutes<10) minutes = "0"+minutes;
    if(seconds<10) seconds = "0"+seconds;

    var result = hours+":"+minutes+":"+seconds;
    return result;
}

$(function(){
    $("#passTest").submit(function(event){
        var endTime = getCurrentTime();
        $("#passTest").append('<input hidden name="endTime" value="'+endTime+'">');
    });
});

function getFromBundle(property){
    var result;
    $.ajax({
        type:"GET",
        url:"/getProperty?property="+property,
        data:property,
        mimeType:"text/plain; charset=UTF-8",
        async:false,
        success:function(response){
            result = response;
        }
    });

    if(result == null){
        return "";
    }else{
        return result;
    }
}

function createQuestion() {
    var parentObject = $(".questionBlock")[0];
    var questions = $(parentObject).find("div[id^='question#']");
    var questionLength = questions.length;

    var questionNumber = getFromBundle("form.label.questionNumber");
    var questionPlaceholder = getFromBundle("form.placeholder.question");
    var addAnswer = getFromBundle("form.button.addAnswer");
    var answerPlaceholder = getFromBundle("form.placeholder.answer");

    if (questionLength == null) {
        questionLength = 0;
    }

    $(parentObject)
        .prepend($('<div id="question#' + questionLength + '" class="question"></div>')
            .prepend($('<h4 class="questionHeader col-lg-6">' + questionNumber + (questionLength + 1) + '</h4>'))
            .append($('<button class="removeQuestion btn btn-danger"> - </button>'))
            .append($('<textarea name="questions[' + questionLength + '].name" class="questionName"' +
                'placeholder="' + questionPlaceholder + '"' +
                'maxlength="255" required' +
                '</textarea>'))
            .append($('<button class="createAnswer btn btn-success" type="button">' + addAnswer + '</button>'))
            .append($('<div id="answer#0" class="answers"</div>')
                .append($('<input name="questions[' + questionLength + '].answers[0].right" type="checkbox">'))
                .append($('<textarea class="form-control" name="questions[' + questionLength + '].answers[0].name"' +
                    'placeholder="' + answerPlaceholder + '" maxlength="255" required></textarea><br>'))
            )
        );
}

function removeQuestion(question) {
    $(question).remove();
    var questions = $($(".questionBlock")[0]).find("div[id^='question#']");
    updateQuestions(questions);
}

function updateQuestions(questions) {
    if (questions != null) {
    var questionNumber = getFromBundle("form.label.questionNumber");
        for (var i = 0; i < questions.length; i++) {
            var currentQuestionNumber = questions.length - i - 1;
            questions[i].id = "question#" + currentQuestionNumber;
            var answers = $(questions[i]).find("div[id^='answer#']");
            var questHeader = $(questions[i]).find(":header[class*='questionHeader']")[0];
            var textarea = $(questions[i]).find("textarea[class*='questionName']")[0];

            questHeader.innerHTML = questionNumber+parseInt(currentQuestionNumber+1,10);
            textarea.setAttribute('name','questions['+currentQuestionNumber+'].name')
            updateAnswers(answers);
        }
    }
}
    function createAnswer(question) {
        var questNumber = $(question).attr('id').replace("question#", "");
        var answers = $(question).find("div[id^='answer#']");
        var answerLength = answers.length;

        var answerPlaceholder = getFromBundle("form.placeholder.answer");

        if (answerLength == null) {
            answerLength = 0;
        }

          $((answers)[answerLength -1])
            .after($('<div id="answer#' + answerLength + '" class="answers"></div>')
                .append($('<input name="questions[' + questNumber + '].answers[' + answerLength + '].right" type="checkbox">'))
                .append($('<textarea class="form-control" name="questions[' + questNumber + '].answers['+answerLength+'].name"' +
                                    'placeholder="' + answerPlaceholder + '" maxlength="255" required></textarea><br>'))
                .append($('<button class="removeAnswer btn btn-danger" type="button"> - </button><br>'))
            )
    }

    function removeAnswer(answer) {
        var currentQuestion = $(answer).closest(".question")[0];
        $(answer).remove();

        var currentQuestionNumber = $(currentQuestion).attr('id').replace("question#", "");
        var answers = $(currentQuestion).find("div[id^='answer#']");

        updateAnswers(answers);
    }

    function updateAnswers(answers) {
        if (answers.length != null) {
            for (var i = 0; i < answers.length; i++) {
                var answerNum = answers.length - i - 1;
                var currentQuestionNumber = $($(answers[i]).closest(".question")[0]).attr('id').replace("question#","");
                var inputCheckBox = $(answers[i]).find("input:checkbox")[0];
                var answerId = $(answers[i]).find("input:text")[0];
                var textarea = $(answers[i]).find("textarea")[0];
                var newCheckbox = $(inputCheckBox).clone()[0];
                answers[i].id = "answer#" + answerNum;

                newCheckbox.setAttribute('name','questions[' + currentQuestionNumber + '].answers[' + answerNum + '].right');
                newCheckbox.setAttribute('value',$(inputCheckBox).val());

                var newTextarea = $(textarea).clone()[0];
                var newAnswerId;

                if(answerId != null){
                    newAnswerId = answerId.clone()[0];
                    newAnswerId.setAttribute('name','questions['+currentQuestionNumber+'].answers['+answerNum+'].id');
                    newAnswerId.setAttribute('value',$(answerId).val());

                    answerId.parentNode.replaceChild(newAnswerId,answerId);
                }
                
                newTextarea.setAttribute('name', 'questions[' + currentQuestionNumber + '].answers[' + answerNum + '].name');
                newTextarea.setAttribute('value', $(textarea).val());
                
                inputCheckBox.parentNode.replaceChild(newCheckbox, inputCheckBox);
                textarea.parentNode.replaceChild(newTextarea,textarea);
                
            }
        }
    }

    $(function () {
        $(".createQuestion").click(function () {
            createQuestion();
        })
    });

    $(function () {
        $(".questionBlock").on("click", ".createAnswer", function () {
            createAnswer($(this).closest(".question")[0]);
        })
    });

    $(function () {
        $(".questionBlock").on("click", ".removeAnswer", function () {
            removeAnswer($(this).closest(".answers")[0]);
        })
    });

    $(function(){
        $(".questionBlock").on("click",".removeQuestion",function(){
            removeQuestion($(this).closest(".question")[0]);
        })
    });

    //download test as text document
    $(function(){
        $(".download").click(function(){
            var result;
            var testId = $($(this).closest("tr").find("td")[0]).attr("id");
                $(this).attr({target: '_blank', href: "/getDocument?testId="+testId});
        })
    });

    $('.download').click(function () {
        var testId = $($(this).closest("tr").find("td")[0]).attr("id");
        $.ajax({
            url: "/getDocument?testId="+testId,
            method: 'GET',
            mimeType:'charset=utf-8',
            xhrFields: {
                responseType: 'blob'
            },
            success: function (data,textStatus,xhr) {
                var a = document.createElement('a');
                var url = window.URL.createObjectURL(data);
                var filename = "";
                var disposition = xhr.getResponseHeader('Content-Disposition');
                if (disposition && disposition.indexOf('attachment') !== -1) {
                    var filenameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
                    var matches = filenameRegex.exec(disposition);
                    if (matches != null && matches[1]) filename = matches[1].replace(/['"]/g, '');
                }
                a.href = url;
                a.download = filename;
                document.body.append(a);
                a.click();
                a.remove();
                window.URL.revokeObjectURL(url);
            }
        });
    });

