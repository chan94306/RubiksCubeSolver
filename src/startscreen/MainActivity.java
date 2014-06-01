package startscreen;

import solver.SolverActivity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import andy_andrew.rubiks.R;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


	}

	public void goToActivity2(View view){
		Intent i = new Intent(this , SolverActivity.class);
		startActivity(i);
	}

	public void contactClick(View view){
//		String uriText = "mailto:" + Uri.encode("cbell@pausd.org") + 
//				"?subject=" + Uri.encode("JetBoi") + 
//				"&body=" + Uri.encode("Sample Question: Who discovered the Galilean moons?");
//		Intent mail = new Intent(Intent.ACTION_SENDTO, Uri.parse(uriText));
//
//		startActivity(Intent.createChooser(mail, "Send mail..."));
	}


}
