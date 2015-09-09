package colorpalette;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;
import android.widget.Button;

/**
 * Model for a Button in the color Palette
 * @author andy
 *
 */
public class PaletteButton extends Button {

	private int color;

	public PaletteButton(Context context, final ColorPalette colorPalette, int color) {
		super(context);
		this.color = color;

		setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				colorPalette.notifyColorSelection(getColor(), getX(), getY());
			}
		});
	}

	public int getColor(){
		return color;
	}
		
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}
}