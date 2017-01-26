package imartinez.com.spacematerial.peopleinspace;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import imartinez.com.spacematerial.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PersonInSpaceDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonInSpaceDetailFragment extends Fragment {
    private static final String ARG_PERSON_IN_SPACE = "person_in_space";

    private PersonInSpace personInSpace;

    @BindView(R.id.person_in_space_detail_name_textview)
    TextView nameTextView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param personInSpace {@link PersonInSpace} to represent.
     * @return A new instance of fragment PersonInSpaceDetailFragment.
     */
    public static PersonInSpaceDetailFragment newInstance(PersonInSpace personInSpace) {
        PersonInSpaceDetailFragment fragment = new PersonInSpaceDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PERSON_IN_SPACE, personInSpace);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            personInSpace = (PersonInSpace) getArguments().getSerializable(ARG_PERSON_IN_SPACE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person_in_space_detail, container, false);
        ButterKnife.bind(this, view);

        nameTextView.setText(personInSpace.name());

        return view;
    }
}
