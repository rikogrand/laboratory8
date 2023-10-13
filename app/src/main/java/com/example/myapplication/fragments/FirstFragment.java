package com.example.myapplication.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.R;
import com.example.myapplication.GameAdapter;
import com.example.myapplication.databinding.FragmentFirstListBinding;


public class FirstFragment extends Fragment {
    public FirstFragment() {
        super(R.layout.fragment_first);
    }

    private ViewModelGame viewmodel;
    public GameAdapter gameAdapter;


    private FragmentFirstListBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFirstListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewmodel = new ViewModelProvider(requireActivity()).get(ViewModelGame.class);
        FragmentActivity activity = requireActivity();
        Context context = requireContext();
      Thread thread = new Thread(() -> {
          gameListInit(activity, context);
          try {
              Thread.sleep(3000);
          } catch (InterruptedException e)
          {
              e.printStackTrace();
          }
          activity.runOnUiThread( () -> {
              view.findViewById(R.id.load_bar).setVisibility(View.GONE);
              view.findViewById(R.id.list).setVisibility(View.VISIBLE);
          });
      });
      thread.start();

        ShowNewGameFragment();
        ShowSettingsFragment();
    }

    private void gameListInit(FragmentActivity activity, Context context) {
        if (gameAdapter == null) {
            gameAdapter = new GameAdapter(
                    context,
                    R.layout.list_item,
                    viewmodel.gameList);
        }
        binding.list.setOnItemClickListener((parent, view, position, id) -> {
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.main_fragment, InfoGameFragment.newInstance(gameAdapter.getItem(position)), "fragment_town_details")
                    .addToBackStack("fragment_town_details")
                    .commit();
        });
        binding.list.setAdapter(gameAdapter);
        binding.list.setOnItemLongClickListener((adapterView, view, i, l) -> {
            Toast.makeText(getContext(),
                    String.valueOf(i),
                    Toast.LENGTH_LONG).show();
            gameAdapter.remove(gameAdapter.getItem(i));
            return true;
        });

    }

    public void ShowNewGameFragment() {
        binding.addGame.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_fragment, new NewGameFragment(gameAdapter), "new_game_fragment")
                    .addToBackStack("new_game_fragment")
                    .commit();
        });

    }

    public void ShowSettingsFragment() {
        binding.settingsBtn.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.main_fragment, new SettingsFragment(gameAdapter), "fragment_settings")
                    .addToBackStack("fragment_settings")
                    .commit();
        });
        }

}
