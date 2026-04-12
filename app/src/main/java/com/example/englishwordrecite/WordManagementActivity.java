package com.example.englishwordrecite;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WordManagementActivity extends AppCompatActivity {

    private EditText etSearch;
    private Button btnSearch, btnAddWord, btnImportWords, btnExportWords;
    private ListView lvWords;
    private WordAdapter wordAdapter;
    private List<Word> wordList;
    private DatabaseHelper dbHelper;
    private WordDao wordDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_management);

        etSearch = findViewById(R.id.etSearch);
        btnSearch = findViewById(R.id.btnSearch);
        btnAddWord = findViewById(R.id.btnAddWord);
        btnImportWords = findViewById(R.id.btnImportWords);
        btnExportWords = findViewById(R.id.btnExportWords);
        lvWords = findViewById(R.id.lvWords);

        dbHelper = new DatabaseHelper(this);
        wordDao = new WordDao(dbHelper.getWritableDatabase());

        loadWords();

        wordAdapter = new WordAdapter(wordList);
        lvWords.setAdapter(wordAdapter);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchWords();
            }
        });

        btnAddWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditWordDialog(null);
            }
        });

        btnImportWords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 打开文件选择器
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("text/*");
                startActivityForResult(intent, 1001);
            }
        });

        btnExportWords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 导出词库为CSV文件
                exportWordsToCsv();
            }
        });

        lvWords.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Word word = wordList.get(position);
                showEditWordDialog(word);
            }
        });

        lvWords.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Word word = wordList.get(position);
                showDeleteWordDialog(word);
                return true;
            }
        });
    }

    private void loadWords() {
        wordList = wordDao.getAllWords();
    }

    private void searchWords() {
        String keyword = etSearch.getText().toString().trim();
        if (keyword.isEmpty()) {
            loadWords();
        } else {
            wordList = wordDao.searchWords(keyword);
        }
        wordAdapter.notifyDataSetChanged();
    }

    private void showEditWordDialog(final Word word) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_edit_word, null);
        builder.setView(view);

        final EditText etEnglish = view.findViewById(R.id.etEnglish);
        final EditText etChinese = view.findViewById(R.id.etChinese);
        final Spinner spinnerDifficulty = view.findViewById(R.id.spinnerDifficulty);
        Button btnCancel = view.findViewById(R.id.btnCancel);
        Button btnSave = view.findViewById(R.id.btnSave);

        // 设置难度选择器
        ArrayAdapter<String> difficultyAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, new String[]{"1", "2", "3", "4", "5"});
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDifficulty.setAdapter(difficultyAdapter);

        if (word != null) {
            etEnglish.setText(word.getEnglish());
            etChinese.setText(word.getChinese());
            spinnerDifficulty.setSelection(word.getDifficulty() - 1);
        }

        final AlertDialog dialog = builder.create();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String english = etEnglish.getText().toString().trim();
                String chinese = etChinese.getText().toString().trim();
                int difficulty = spinnerDifficulty.getSelectedItemPosition() + 1;

                if (english.isEmpty() || chinese.isEmpty()) {
                    Toast.makeText(WordManagementActivity.this, "请填写完整信息", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (word == null) {
                    // 添加新单词
                    Word newWord = new Word(english, chinese);
                    newWord.setDifficulty(difficulty);
                    wordDao.addWord(newWord);
                    Toast.makeText(WordManagementActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                } else {
                    // 更新单词
                    word.setEnglish(english);
                    word.setChinese(chinese);
                    word.setDifficulty(difficulty);
                    wordDao.updateWord(word);
                    Toast.makeText(WordManagementActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
                }

                loadWords();
                wordAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showDeleteWordDialog(final Word word) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("删除单词");
        builder.setMessage("确定要删除单词 " + word.getEnglish() + " 吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                wordDao.deleteWord(word.getId());
                loadWords();
                wordAdapter.notifyDataSetChanged();
                Toast.makeText(WordManagementActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    private class WordAdapter extends ArrayAdapter<Word> {

        public WordAdapter(List<Word> words) {
            super(WordManagementActivity.this, 0, words);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_word, parent, false);
            }

            Word word = getItem(position);

            TextView tvEnglish = convertView.findViewById(R.id.tvEnglish);
            TextView tvChinese = convertView.findViewById(R.id.tvChinese);
            TextView tvDifficulty = convertView.findViewById(R.id.tvDifficulty);
            TextView tvTestCount = convertView.findViewById(R.id.tvTestCount);
            TextView tvAccuracy = convertView.findViewById(R.id.tvAccuracy);

            tvEnglish.setText(word.getEnglish());
            tvChinese.setText(word.getChinese());
            tvDifficulty.setText("难度: " + word.getDifficulty());
            tvTestCount.setText("考试: " + word.getTestCount() + "次");
            tvAccuracy.setText("正确率: " + String.format("%.1f%%", word.getAccuracy() * 100));

            return convertView;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                importWordsFromCsv(uri);
            }
        }
    }

    // 从CSV文件导入单词
    private void importWordsFromCsv(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            List<Word> words = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String english = parts[0].trim();
                    String chinese = parts[1].trim();
                    int difficulty = 1;
                    if (parts.length >= 3) {
                        try {
                            difficulty = Integer.parseInt(parts[2].trim());
                        } catch (NumberFormatException e) {
                            difficulty = 1;
                        }
                    }
                    Word word = new Word(english, chinese);
                    word.setDifficulty(difficulty);
                    words.add(word);
                }
            }

            reader.close();
            inputStream.close();

            if (!words.isEmpty()) {
                wordDao.batchAddWords(words);
                loadWords();
                wordAdapter.notifyDataSetChanged();
                Toast.makeText(this, "成功导入 " + words.size() + " 个单词", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "没有找到有效的单词数据", Toast.LENGTH_SHORT).show();
            }

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "导入失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // 导出单词为CSV文件
    private void exportWordsToCsv() {
        try {
            List<Word> words = wordDao.getAllWords();
            if (words.isEmpty()) {
                Toast.makeText(this, "词库为空，无法导出", Toast.LENGTH_SHORT).show();
                return;
            }

            // 创建导出目录
            File exportDir = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "word_export");
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }

            // 创建CSV文件
            String fileName = "word_list_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".csv";
            File csvFile = new File(exportDir, fileName);

            // 写入CSV文件
            FileWriter writer = new FileWriter(csvFile);
            writer.write("English,Chinese,Difficulty,TestCount,CorrectCount,Accuracy\n");

            for (Word word : words) {
                writer.write(word.getEnglish() + "," + word.getChinese() + "," + word.getDifficulty() + "," +
                        word.getTestCount() + "," + word.getCorrectCount() + "," + word.getAccuracy() + "\n");
            }

            writer.close();

            // 显示导出成功信息
            Toast.makeText(this, "词库已导出到：" + csvFile.getAbsolutePath(), Toast.LENGTH_LONG).show();

            // 打开文件所在目录
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(exportDir), "resource/folder");
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "导出失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }

    public static void start(android.content.Context context) {
        android.content.Intent intent = new android.content.Intent(context, WordManagementActivity.class);
        context.startActivity(intent);
    }
}
