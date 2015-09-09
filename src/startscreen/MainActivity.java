package startscreen;

import solver.SolverActivity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import andy_andrew.rubiks.R;

/**
 * The main activity that launches the app
 */
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	/**
	 * Launches the SolverActivity
	 * @param view
	 */
	public void launchSolverActivity(View view){
		Intent i = new Intent(this, SolverActivity.class);
		startActivity(i);
	}

	/**
	 * Launches an email intent to for users to send feedback about the app
	 * @param view
	 */
	public void contactClick(View view){
		String uriText = "mailto:" + Uri.encode("y.zhang@berkeley.edu") + 
				"?subject=" + Uri.encode("Rubik's Cube Solver App") + 
				"&body=" + Uri.encode("We'd love to hear your feedback!");
		Intent mail = new Intent(Intent.ACTION_SENDTO, Uri.parse(uriText));

		startActivity(Intent.createChooser(mail, "Select Mail Client..."));
	}


}
