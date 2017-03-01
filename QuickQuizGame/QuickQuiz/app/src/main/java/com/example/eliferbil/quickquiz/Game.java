package com.example.eliferbil.quickquiz;

import java.util.ArrayList;

/**
 * Created by eliferbil on 26/02/2017.
 */

public class Game {

    private User user;
    private boolean gameContinued; //TODO: username girilince true olacak, sorular bitince false olacak, bittiği zaman scoreActivity açılacak
                                   //TODO: app arkaplana geçince veya appten çıkıldığında kaldığı yerden kullanılması için de kullanılabilir
    private int questionsAnswered; //TODO: her soru cevaplandığında arttırılacak, 15 olduğunda scoreActivity açılacak
    private ArrayList<Question> historyList;
    private ArrayList<Question> musicList;
    private ArrayList<Question> foodList;
    private static Game instance = null;

    protected Game(){
        this.user = null;
        this.gameContinued = false;
        questionsAnswered = 0;
        this.historyList = generateHistoryQuestions();
        this.foodList = generateFoodQuestions();
        this.musicList = generateMusicQuestions();

    }

    public static Game getInstance() {
        if(instance == null) {
            instance = new Game();
        }
        return instance;
    }

    public ArrayList<Question> generateHistoryQuestions(){
        ArrayList<Question> questions = new ArrayList<Question>();
        ArrayList<Answer> firstQ = new ArrayList<Answer>();
        firstQ.add(new Answer("1923"));
        firstQ.add(new Answer("1938"));
        firstQ.add(new Answer("1917"));
        firstQ.add(new Answer("1914", true));
        questions.add(new Question("history","World War I began in which year?",100, firstQ));
        ArrayList<Answer> secondQ = new ArrayList<Answer>();
        secondQ.add(new Answer("France"));
        secondQ.add(new Answer("Germany"));
        secondQ.add(new Answer("Hungary"));
        secondQ.add(new Answer("Austria", true));
        questions.add(new Question("history","Adolf Hitler was born in which country?",200, secondQ));
        ArrayList<Answer> thirdQ = new ArrayList<Answer>();
        thirdQ.add(new Answer("Johannes Gutenburg",true));
        thirdQ.add(new Answer("Benjamin Franklin"));
        thirdQ.add(new Answer("Sir Isaac Newton"));
        thirdQ.add(new Answer("Martin Luther"));
        questions.add(new Question("history","The first successful printing press was developed by this man.",300, thirdQ));
        ArrayList<Answer> fourthQ = new ArrayList<Answer>();
        fourthQ.add(new Answer("Marcus Aurelius"));
        fourthQ.add(new Answer("Hadrian",true));
        fourthQ.add(new Answer("Nero"));
        fourthQ.add(new Answer("Augustus"));
        questions.add(new Question("history","Which Roman Emperor built a massive wall across Northern Britain in 122 A.D.?",400, fourthQ));
        ArrayList<Answer> fifthQ = new ArrayList<Answer>();
        fifthQ.add(new Answer("Julius Caesar"));
        fifthQ.add(new Answer("Hannibal"));
        fifthQ.add(new Answer("William the Conqueror"));
        fifthQ.add(new Answer("Attila the Hun", true));
        questions.add(new Question("history","What famous 5th century A.D conqueror was known as 'The Scourge of God'?",500, fifthQ));
        return questions;
    }

    public ArrayList<Question> generateMusicQuestions(){
        ArrayList<Question> questions = new ArrayList<Question>();
        ArrayList<Answer> firstQ = new ArrayList<Answer>();
        firstQ.add(new Answer("C"));
        firstQ.add(new Answer("K",true));
        firstQ.add(new Answer("F"));
        firstQ.add(new Answer("G"));
        questions.add(new Question("music","Which of these letters does not represent a musical note?",100, firstQ));
        ArrayList<Answer> secondQ = new ArrayList<Answer>();
        secondQ.add(new Answer("Tom Hamilton"));
        secondQ.add(new Answer("Jon Bon Jovi"));
        secondQ.add(new Answer("Axl Rose"));
        secondQ.add(new Answer("Steven Tyler", true));
        questions.add(new Question("music","Who is lead singer of the rock band Aerosmith?",200, secondQ));
        ArrayList<Answer> thirdQ = new ArrayList<Answer>();
        thirdQ.add(new Answer("Franz Ferdinand"));
        thirdQ.add(new Answer("Black Crowes"));
        thirdQ.add(new Answer("Oasis",true));
        thirdQ.add(new Answer("Blur"));
        questions.add(new Question("music","Noel Gallagher and his younger brother Liam Gallagher formed which rock band?.",300, thirdQ));
        ArrayList<Answer> fourthQ = new ArrayList<Answer>();
        fourthQ.add(new Answer("Hells Bells"));
        fourthQ.add(new Answer("Another One Bites the Dust",true));
        fourthQ.add(new Answer("Amazing Grace"));
        fourthQ.add(new Answer("Stairway to Heaven"));
        questions.add(new Question("music","What Queen son is supposedly can be played backward to reveal that 'It's fun to smoke marijuana'?",400, fourthQ));
        ArrayList<Answer> fifthQ = new ArrayList<Answer>();
        fifthQ.add(new Answer("AC/DC",true));
        fifthQ.add(new Answer("David Lee Roth"));
        fifthQ.add(new Answer("Third Eye Blind"));
        fifthQ.add(new Answer("Pussycat Dolls"));
        questions.add(new Question("music","Who provided music for the 'Iron Man 2 Soundtrack?'",500, fifthQ));
        return questions;
    }

    public ArrayList<Question> generateFoodQuestions(){
        ArrayList<Question> questions = new ArrayList<Question>();
        ArrayList<Answer> firstQ = new ArrayList<Answer>();
        firstQ.add(new Answer("Potato"));
        firstQ.add(new Answer("Choko",true));
        firstQ.add(new Answer("Carrot"));
        firstQ.add(new Answer("Yam"));
        questions.add(new Question("food","Which of the following is not a root vegetable?",100, firstQ));
        ArrayList<Answer> secondQ = new ArrayList<Answer>();
        secondQ.add(new Answer("Oregano"));
        secondQ.add(new Answer("Basil",true));
        secondQ.add(new Answer("Savory"));
        secondQ.add(new Answer("Chives"));
        questions.add(new Question("food","What herb, oddly associated with scorpions, is an ingredient of pesto?",200, secondQ));
        ArrayList<Answer> thirdQ = new ArrayList<Answer>();
        thirdQ.add(new Answer("Flour"));
        thirdQ.add(new Answer("Butter"));
        thirdQ.add(new Answer("Egg White",true));
        thirdQ.add(new Answer("Milk"));
        questions.add(new Question("food","What is added to sugar to make meringue?.",300, thirdQ));
        ArrayList<Answer> fourthQ = new ArrayList<Answer>();
        fourthQ.add(new Answer("Chestnut"));
        fourthQ.add(new Answer("Walnut",true));
        fourthQ.add(new Answer("Pecan"));
        fourthQ.add(new Answer("Coconut"));
        questions.add(new Question("food","What kind of nuts is used in Waldorf Salad?",400, fourthQ));
        ArrayList<Answer> fifthQ = new ArrayList<Answer>();
        fifthQ.add(new Answer("Rice",true));
        fifthQ.add(new Answer("Bread"));
        fifthQ.add(new Answer("Pasta"));
        fifthQ.add(new Answer("Potato"));
        questions.add(new Question("food","Arborio is a variety of which staple food?'",500, fifthQ));
        return questions;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList<Question> getHistoryQuestions() {
        return historyList;
    }

    public ArrayList<Question> getMusicQuestions() {
        return musicList;
    }

    public ArrayList<Question> getFoodQuestions() {
        return foodList;
    }
}
