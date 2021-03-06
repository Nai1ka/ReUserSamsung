package ru.ndevelop.reusersamsung.ui.tagsList;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import io.sulek.ssml.SSMLLinearLayoutManager;
import ru.ndevelop.reusersamsung.App;
import ru.ndevelop.reusersamsung.R;
import ru.ndevelop.reusersamsung.core.adapters.TagListAdapter;
import ru.ndevelop.reusersamsung.core.enums.ButtonType;
import ru.ndevelop.reusersamsung.core.interfaces.OnEditButtonClickListener;
import ru.ndevelop.reusersamsung.core.interfaces.TagDao;
import ru.ndevelop.reusersamsung.repositories.AppDatabase;
import ru.ndevelop.reusersamsung.ui.MainActivity;

public class TagListFragment extends Fragment implements OnEditButtonClickListener {


    private RecyclerView recyclerView;
    private TagListAdapter tagListAdapter;
    private AppDatabase database;
    TagDao tagDao;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tagListAdapter = new TagListAdapter(this);
        database = App.getInstance().getDatabase();
        tagDao = database.getTagDao();
    }

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_taglist, container, false);
        initViews(root);
        return root;
    }

    void initViews(View root) {
        recyclerView = root.findViewById(R.id.rv_taglist);
        recyclerView.setAdapter(tagListAdapter);
        recyclerView.setLayoutManager(new SSMLLinearLayoutManager(requireContext()));

    }

    private void showDeleteConfirmationDialog(String tagId) {
        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setTitle("Вы уверены, что хотите удалить эту метку?")
                .setMessage("Это действие нельзя отменить")
                .setPositiveButton("Ок", (dialog1, which) -> {
                   tagDao.delete(tagId);
                    tagListAdapter.loadItems(tagDao.getAllTags());
                    tagListAdapter.notifyDataSetChanged();
                })
                .setNegativeButton("Не надо", null)
                .create();
        dialog.show();
    }

    @Override
    public void onEditButtonClick(ButtonType button, String tagId) {
        switch (button) {
            case EDIT:
                ((MainActivity) requireActivity()).startActionsSelectionActivity(tagId);
                break;
            case DELETE:
                showDeleteConfirmationDialog(tagId);
                break;


        }
    }

    @Override
    public void onResume() {
        super.onResume();
        tagListAdapter.loadItems(tagDao.getAllTags());
    }
}