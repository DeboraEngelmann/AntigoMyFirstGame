package br.com.memorygame.myfirstgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import java.util.ArrayList;
import java.util.Collections;

import br.com.memorygame.myfirstgame.Dao.JogadorDao;
import br.com.memorygame.myfirstgame.Dao.LevelDao;
import br.com.memorygame.myfirstgame.Entidades.Level;

public class JogoLevels extends Activity {
    private GridView gridViewImagem;
    public static AdapterListView mAdapter;
    static ArrayList<Integer> arrayAdapter = MyFirstGame.arrayAdapter;
    static ArrayList<Integer> clicou = MyFirstGame.clicou;
    static ArrayList<Integer> imagemList2 = MyFirstGame.imagemList2;
    static ArrayList<Integer> imagemList = MyFirstGame.imagemList;
    static int contJogada =0;
    static public int idLevel;
    private static int fim;
    private LevelDao levelDao;
    private static Level level;



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contJogada=0;
        levelDao = LevelDao.getInstance(getBaseContext());
        level = levelDao.getLevel(idLevel);

        setContentView(R.layout.jogo_levels);
        gridViewImagem = (GridView) findViewById(R.id.gridViewImagens);


        if(savedInstanceState==null){
            limparArrays();
            popularArrays();
            sortear();
            atualizaAdapter();


        }else{
            gridViewImagem.setAdapter(mAdapter);
        }


        gridViewImagem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int cont=0;
                contJogada++;
                for (int i=0;i<clicou.size();i++){
                    if (clicou.get(i)==1){
                        cont++;
                    }
                }
                if (cont==1){
                    //verificar se as imagens abertas são iguais.
                    for (int i=0;i<clicou.size();i++){
                        if (clicou.get(i)==1){
                            if (i!=position){
                                if (imagemList2.get(i).equals(imagemList2.get(position))){
                                    clicou.set(position, 3);//manter as imagens abertas
                                    clicou.set(i, 3);
                                    arrayAdapter.set(position,imagemList2.get(position));
                                    Log.i("Entrou", "Entrou " + imagemList2.get(i).toString() +" "+ imagemList2.get(position).toString());
                                }
                            }

                        }
                    }
                }//esconder a imagem após abrir duas diferentes.
                if (cont==2){

                    for (int i=0;i<clicou.size();i++){
                        if (clicou.get(i)==1){
                            if (clicou.get(i) != 3) {

                                arrayAdapter.set(i, R.drawable.logo);
                                clicou.set(i, 2);
                            }
                        }
                    }
                }
                if (clicou.get(position) == 2) {

                    arrayAdapter.set(position,imagemList2.get(position));
                    clicou.set(position, 1);
                } else if (clicou.get(position)==1){
                    arrayAdapter.set(position, R.drawable.logo);
                    clicou.set(position, 2);
                }

                mAdapter.notifyDataSetChanged();

                fim =0;
                for (int i=0;i<clicou.size();i++){
                    if (clicou.get(i)==3) {
                        fim = fim + 1;
                    }
                    if (clicou.size()==fim){
                        if (MainActivity.jogador.getProgresso()<idLevel){
                            MainActivity.jogador.setProgresso(1);
                            JogadorDao jogadorDao = JogadorDao.getInstance(getBaseContext());
                            jogadorDao.updateJogador(MainActivity.jogador);
                            level.setTentativas(1);
                            level.setJogadasLevel(contJogada/2);
                            levelDao.updateLevel(level);
                            Level proximoLevel = new Level();

                           // if (levelDao.getLevel(MainActivity.jogador.getProgresso()).getIdLevel()<5){
                                proximoLevel = levelDao.getLevel(MainActivity.jogador.getProgresso() +1);
                                proximoLevel.setConcluido(1);

                                levelDao.updateLevel(proximoLevel);
                          //  }

                        }
                        level.setTentativas(1);
                        if ((contJogada/2)< level.getJogadasLevel()){
                            level.setJogadasLevel(contJogada/2);
                        }
                        levelDao.updateLevel(level);

                        Intent ranking = new Intent(JogoLevels.this, Progresso.class);
                        ranking.putExtra("idLevel",idLevel);
                        startActivity(ranking);
                        finish();

                    }
                }


            }
        });

    }

    private void atualizaAdapter(){
        mAdapter = new AdapterListView(JogoLevels.this, arrayAdapter);
        gridViewImagem.setAdapter(mAdapter);

        for (int i = 0; i < MyFirstGame.getNumImg(idLevel); i++) {
            imagemList2.add(imagemList.get(i));
        }
        imagemList2.addAll(imagemList2);
        Collections.shuffle(imagemList2);
    }

    private void limparArrays(){
        arrayAdapter.clear();
        clicou.clear();
        imagemList2.clear();
    }

    private void popularArrays(){

        //Popular arrayAdapter
        for (int i = 0; i < MyFirstGame.getNumImg(idLevel); i++) {
            arrayAdapter.add(R.drawable.logo);
            clicou.add(2);
        }
        arrayAdapter.addAll(arrayAdapter);
        clicou.addAll(clicou);
    }

    private void sortear(){

        //Sorteio de Imagens
        Collections.shuffle(imagemList);
        //Sorteio de Posição
        Collections.shuffle(arrayAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putIntegerArrayList("arrayAdapter", arrayAdapter);
        savedInstanceState.putIntegerArrayList("clicou",clicou);
        savedInstanceState.putIntegerArrayList("imagemList2",imagemList2);
        savedInstanceState.putIntegerArrayList("imagemList",imagemList);
        super.onSaveInstanceState(savedInstanceState);
    }
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        arrayAdapter = savedInstanceState.getIntegerArrayList("arrayAdapter");
        clicou = savedInstanceState.getIntegerArrayList("clicou");
        imagemList2 =savedInstanceState.getIntegerArrayList("imagemList2");
        imagemList=savedInstanceState.getIntegerArrayList("imagemList");

    }
}
