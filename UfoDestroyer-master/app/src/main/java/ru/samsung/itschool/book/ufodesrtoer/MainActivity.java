package ru.samsung.itschool.book.ufodesrtoer;

import android.app.Activity;
import android.os.Bundle;

import ru.samsung.itschool.book.ufodesrtoer.GameView;
import ru.samsung.itschool.book.ufodesrtoer.model.Game;
import ru.samsung.itschool.book.ufodesrtoer.model.GameImpl;

public class MainActivity extends Activity {

    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        game = new GameImpl();
        setContentView(new GameView(game, this));
    }
}
