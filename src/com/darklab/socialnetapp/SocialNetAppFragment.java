package com.darklab.socialnetapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.darklab.socialnetapp.R.id;

public class SocialNetAppFragment extends SocialNetBaseFragment implements
		ResponceListener {

	public static final String TAG = "SocialNetAppFragment";

	Button btnRequestOne, btnRequestTwo, btnCreateEvent;
	TextView textViewMain;
	EditText editTextIdentifier, editTextRant;
	Spinner spinnerProviderName, spinnerFeeling;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_social_net_app, container,
				false);

		textViewMain = (TextView) v.findViewById(id.textViewMain);
		editTextIdentifier = (EditText) v.findViewById(id.editTextIdentifier);

		spinnerProviderName = (Spinner) v.findViewById(id.spinnerProviderName);
		initializeSpinner(spinnerProviderName, R.array.provider_name);

		btnRequestOne = (Button) v.findViewById(id.btnRequestOne);
		btnRequestOne.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String identifier = editTextIdentifier.getText().toString();
				TextView view = (TextView) spinnerProviderName
						.getSelectedView();
				String provider = view.getText().toString();
				if (isValid(provider) && isValid(identifier)) {
					RequestMaker.loginWithSocialId(SocialNetAppFragment.this,
							provider, identifier);
				} else {
					Toast.makeText(getActivity(),
							"Check Provider and Identifier", Toast.LENGTH_LONG)
							.show();
				}

			}
		});

		btnRequestTwo = (Button) v.findViewById(id.btnRequestTwo);
		btnRequestTwo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				RequestMaker.findAllUsers(SocialNetAppFragment.this);

			}
		});

		spinnerFeeling = (Spinner) v.findViewById(id.spinnerFeeling);
		initializeSpinner(spinnerFeeling, R.array.feeling);

		editTextRant = (EditText) v.findViewById(id.editTextRant);

		btnCreateEvent = (Button) v.findViewById(id.btnCreateEvent);
		btnCreateEvent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String identifier = editTextIdentifier.getText().toString();
				TextView view = (TextView) spinnerProviderName
						.getSelectedView();
				String provider = view.getText().toString();
				String feeling = ((TextView) spinnerFeeling.getSelectedView())
						.getText().toString();
				String rant = editTextRant.getText().toString();
				if (isValid(provider) && isValid(identifier)
						&& isValid(feeling)) {
					RequestMaker.createEvent(SocialNetAppFragment.this, null,
							null, provider, identifier, feeling, rant);

				} else {
					Toast.makeText(getActivity(),
							"Check Provider, Identifier, Feeling",
							Toast.LENGTH_LONG).show();
				}

			}
		});

		return v;
	}

	private void initializeSpinner(Spinner spinner, int stringArrayId) {
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				getActivity(), stringArrayId,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}

	@Override
	public void onResponceReceived(String responce, HttpRequest request) {
		String string = "";
		if (request.getType().equals(HttpRequest.Type.LoginWithSocialId)) {
			string = "Login - ";
		} else if (request.getType().equals(HttpRequest.Type.FindAllUsers)){
			string = "Find - ";
		} else if (request.getType().equals(HttpRequest.Type.CreateEvent)){
			string = "Create - ";
		}
		logResult(string + responce);
		textViewMain.setText(string + responce);

	}

}
