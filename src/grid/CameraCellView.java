package grid;

import colorpalette.ColorPalette;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;

public class CameraCellView extends CellView{
	public CameraCellView(Activity activity, int x, int y, int length, int thickness, int rx, 
			int margin, int edgeColor, final ColorPalette colorPalette) {
		
		super(activity, x, y, length, thickness, rx, margin, edgeColor);
		fillButton.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				if (colorPalette.getSelectedColor() != ColorPalette.NO_COLOR) {
					fillButton.setBackgroundColor(colorPalette.getSelectedColor());
				}
			}
		});
	}
}
