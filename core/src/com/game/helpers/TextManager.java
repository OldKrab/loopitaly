package com.game.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import sun.font.TrueTypeFont;

public class TextManager {
    private String message;
    private Vector2 position;
    int align;
    GlyphLayout glyphLayout;


    private BitmapFont font;
    private static final String FONT_CHARS = "абвгдежзийклмнопрстуфхцчшщъыьэюяabcdefghijklmnopqrstuvwxyz" +
            "АБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;:,{}\"´`'<>";

    public TextManager(String message, Vector2 position, int size) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.characters = FONT_CHARS;
        parameter.size = size;
        parameter.borderWidth = 1;
        font = generator.generateFont(parameter);
        this.align = Align.center;
        glyphLayout = new GlyphLayout();

        SetText(message, position);
    }
    public TextManager(String message, Vector2 position, int size, int align){
        this(message, position, size);
        this.align = align;
        SetText(message, position);
    }


    public void DisplayMessage(SpriteBatch batch) {
        font.draw(batch, glyphLayout, position.x, position.y);
    }

    public void SetText(String message, Vector2 position) {
        this.message = message;
        this.position = new Vector2(position);
        glyphLayout.setText(font, message, Color.WHITE, 0, align, true);
    }

    public void SetText(String message) {
        this.message = message;
        glyphLayout.setText(font, message, Color.WHITE, 0, align, true);
    }
}
