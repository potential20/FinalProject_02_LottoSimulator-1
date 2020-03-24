package kr.co.tjoeun.finalproject_02_lottosimulator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kr.co.tjoeun.finalproject_02_lottosimulator.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity {

    List<TextView> winNumTxtList = new ArrayList<>();

    int[] winLottoNumArr = new int[6];
    int bonusNum = 0;

    int[] myLottoNumArr = {12, 15, 22, 27, 40, 43};
    long useMoneyAmount = 0;
    long winMoneyAmount = 0;

    ActivityMainBinding binding = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setupEvents();
        setValues();
    }

    @Override
    public void setupEvents() {

        binding.buyOneLottoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                당첨번호를 생성 => 텍스트뷰에 반영
                makeWinLottoNum();
//                몇등인지 판단.
                checkLottoRank();
            }
        });

    }

    @Override
    public void setValues() {

        winNumTxtList.add(binding.winLottoNumTxt01);
        winNumTxtList.add(binding.winLottoNumTxt02);
        winNumTxtList.add(binding.winLottoNumTxt03);
        winNumTxtList.add(binding.winLottoNumTxt04);
        winNumTxtList.add(binding.winLottoNumTxt05);
        winNumTxtList.add(binding.winLottoNumTxt06);
    }

    void makeWinLottoNum() {
//        6개의 숫자(배열) + 보너스번호 1개 int변수
//        => 이 함수에서만이 아니라, 다른곳에서도 쓸 예정.
//        => 당첨 등수 확인때도 사용. => 멤버변수로 배열 /변수 생성

//        당첨번호 + 보너스번호를 모두 0으로 초기화.
//        (이미 뽑은 번호가 있다면 모두 날리자)

        for (int i =0 ; i < winLottoNumArr.length ; i++) {
            winLottoNumArr[i] = 0;
        }
        bonusNum = 0;

//        로또번호 6개 생성.
//        1~45여야 함 + 중복 허용 X

        for (int i=0; i < winLottoNumArr.length ; i++){
//            1~45의 숫자를 랜덤으로 뽑고
//            중복이 아니라면 => 당첨번호로 선정.
//            중복이라면> => 다시 뽑자. => 중복이 아닐때까지 계속 뽑자.

            while (true){

//                1~45의 정수를 랜덤으로 뽑아서 임시저장.
//                이 숫자가 중복검사를 통과하면 사용, 아니면 다시.
                int randomNum = (int) (Math.random() * 45 + 1);

//                중복검사? 당첨번호 전부와 randomNum을 비교.
//                하나라도 같으면 탈락.

                boolean isDuplOk = true; // 중복검사 변수
                for (int winNum : winLottoNumArr){
                    if (winNum == randomNum) {
                        isDuplOk = false; // 탈락!
                        break;
                    }
                }

                if (isDuplOk) {
                    winLottoNumArr[i] = randomNum;
                    Log.i("당첨번호","");
                    break; // 무한반복 탈출.
                }
            }
        }

//        6개의 당첨번호를 작은 숫자부터 정렬.
        Arrays.sort(winLottoNumArr);
        for (int i = 0 ; i < winLottoNumArr.length ; i++) {

            winNumTxtList.get(i).setText(winLottoNumArr[i]+"");
        }

//            보너스번호 생성 => 1~45, 당첨번호 중복 X.

        while (true) {
            int randomNum = (int) (Math.random() * 45 + 1);

            boolean isDuplOk = true;
            for (int winNum : winLottoNumArr) {
                if (winNum == randomNum){
                    isDuplOk = false;
                    break;
                }
            }

            if(isDuplOk) {
                bonusNum = randomNum;
                break;
            }
        }

//        보너스넘버 생성됨.
        binding.bonusNumTxt.setText(bonusNum+"");

    }
    void checkLottoRank() {
//        돈을 천원 지불 + 등수 확인.
        useMoneyAmount += 1000;

        binding.useMoneyTxt.setText(String.format("사용 금액 : %,d원",useMoneyAmount));

//        몇등인지?
//        내 번호를 하나 들고 => 당첨번호 여섯개를 돌아볼것임.
//        얻어낼것? 몇개의 숫자를 맞췄는지.

//        맞춘갯수를 담아줄 변수.
        int correctCount = 0;

        for (int myNum : myLottoNumArr) {
            for (int winNum : winLottoNumArr) {

                if (myNum == winNum) {
                    correctCount++;
                }
            }
        }

//        correctCount의 값에 따라 등수를 판정.
        if (correctCount == 6) {
//            1등 12억
            winMoneyAmount += 1600000000;
        }
        else if (correctCount == 5) {
//            2등 / 3등 재검사 필요 => 보너스번호를 맞췄는지?
        }
        else if (correctCount == 4) {
//            4등
            winMoneyAmount += 50000;
        }
        else if (correctCount == 3) {
//            5등
            useMoneyAmount -= 5000;
        }
        else {
//            꽝!
        }

//        당첨금액 텍스트에도 반영
        binding.winMoneyTxt.setText(String.format("당첨 금액 : %,d원",winMoneyAmount));

    }
}
