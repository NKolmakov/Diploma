var questNumber = 0;
var questionPlaceholder = document.getElementById("form.placeholder.question").value;
var answerPlaceholder = document.getElementById("form.placeholder.answer").value;
var questionNumber = document.getElementById("form.label.questionNumber").value;
var addAnswer = document.getElementById("form.button.addAnswer").value;

$(function (){
    $(".createQuestion").click(function (){
        var html2Add =
        "<div id='question#"+ ++questNumber +"' class='question'>"+
            "<input hidden id='lastAnsIndex#"+questNumber+"' value='1'>"+
            "<h4>"+questionNumber+(questNumber+1)+"</h4>"+
            "<textarea name='questions["+questNumber+"].name' placeholder='"+questionPlaceholder+"' maxlength='255' required></textarea>"+
            "<button id='button#"+questNumber+"' class='createAnswer' type='button'>"+addAnswer+"</button>"+
            "<div id='answers#"+questNumber+"' class='answers'>"+
                "<input name='questions["+questNumber+"].answers[0].name' type='text' placeholder='"+answerPlaceholder+"' maxlength='255' required>"+
            "</div>"+
        "</div>";
        $(".questionBlock").prepend(html2Add);
    })
});

$(function (){
    $(".questionBlock").on("click",".createAnswer",function (){
        var currentQuestionNumber = $(this).attr("id").replace("button#","");
        var currentAnswer = document.getElementById("answers#"+currentQuestionNumber);
        var lastAnswer = document.getElementById("lastAnsIndex#"+currentQuestionNumber);
        lastIndex = lastAnswer.value;

        var html2Add =
         "<input name='questions["+questNumber+"].answers["+lastIndex+"].name' type='text' placeholder='"+answerPlaceholder+"' maxlength='255' required>";
        $(currentAnswer).append(html2Add);
        $(lastAnswer).val(++lastIndex);
    })
});