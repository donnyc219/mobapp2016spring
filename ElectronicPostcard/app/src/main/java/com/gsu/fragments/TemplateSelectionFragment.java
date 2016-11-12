package com.gsu.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gsu.electronicpostcard.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TemplateSelectionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TemplateSelectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TemplateSelectionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ImageView templateImage;


    // TODO: Rename and change types of parameters
    private int mParam1;
//    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public TemplateSelectionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TemplateSelectionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TemplateSelectionFragment newInstance(String imageName) {
        TemplateSelectionFragment fragment = new TemplateSelectionFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM1, imageName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getInt(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//            Log.v("onCreate", "" + mParam1);



        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String imageName = getArguments().getString(ARG_PARAM1);
//        Log.v("onCreateView", "" + imageName);


        View view = inflater.inflate(R.layout.fragment_template_selection, container, false);
        templateImage = (ImageView)view.findViewById(R.id.template_image_view);

        // set the image
        int d = stringToDrawableResId(imageName);
        templateImage.setBackgroundResource(d);

        return view;
    }

    public int stringToDrawableResId(String filename){

        String name = filename;
        int resid = getResources().getIdentifier(name, "drawable", getActivity().getPackageName());

        return resid;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
