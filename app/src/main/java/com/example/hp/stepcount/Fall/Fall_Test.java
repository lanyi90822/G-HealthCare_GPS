package com.example.hp.stepcount.Fall;

import android.util.Log;

/**
 * Created by HP on 2016/12/7.
 */
public class Fall_Test {

    private final static String TAG = "com.example.hp.falltest.TAG";
    private static int counttt = 0;

    private static double[][] transitionMatrix = {{0.386579993382665, 0.166692442718888, 0.446727563898447},
            {0.108613366109935, 0.837506708718509, 0.0538799251715566},
            {1.24470355081331e-08, 6.64115912381346e-18, 0.999999987552964}},
            emissionMatrix = {{0.0447317346526258, 0.954320726196371, 0.000947534692031852, 1.35323041205900e-12, 4.45761800179835e-09},
                    {0.999785743711636, 0.000214256286405376, 1.95880043629917e-12, 7.89520555589155e-32, 1.75102997720033e-19},
                    {8.30986608882873e-05, 0.0421445878580840, 0.336983301492071, 0.163365530260940, 0.457423481728017}};
    private static double[] startMatrix = {0.215467028939723, 0.784532971060277, 3.27302953370115e-70};
    public static int M = transitionMatrix[0].length;
    public static int N = emissionMatrix[0].length;



    public static void Fall_main() {
        // TODO Auto-generated method stub
       // double[]s={1,1,1,1,1,1,1,1,1,1,1.03557128906250,1.03558349609375,1.03875732421875,1.04193115234375,1.04510498046875,1.04661865234375,1.04813232421875,1.04924316406250,1.05035400390625,1.05146484375000,1.05290527343750,1.05434570312500,1.05444335937500,1.05454101562500,1.05463867187500,1.05494384765625,1.05524902343750,1.05479736328125,1.05434570312500,1.05389404296875,1.05324707031250,1.05260009765625,1.05330810546875,1.05401611328125,1.05472412109375,1.05670166015625,1.05867919921875,1.06024169921875,1.06180419921875,1.06336669921875,1.06400146484375,1.06463623046875,1.06370849609375,1.06278076171875,1.06185302734375,1.05909423828125,1.05633544921875,1.05437011718750,1.05240478515625,1.05043945312500,1.04980468750000,1.04916992187500,1.04964599609375,1.05012207031250,1.05059814453125,1.05166015625000,1.05272216796875,1.05407714843750,1.05543212890625,1.05678710937500,1.05749511718750,1.05820312500000,1.05832519531250,1.05786132812500,1.05739746093750,1.05751953125000,1.05764160156250,1.05732421875000,1.05698242187500,1.05664062500000,1.05638427734375,1.05612792968750,1.05587158203125,1.05603027343750,1.05618896484375,1.05704345703125,1.05789794921875,1.05875244140625,1.05938720703125,1.06002197265625,1.06072998046875,1.06143798828125,1.06214599609375,1.06320800781250,1.06427001953125,1.06444091796875,1.06461181640625,1.06478271484375,1.06479492187500,1.06480712890625,1.06419677734375,1.06358642578125,1.06297607421875,1.06135253906250,1.05972900390625,1.05772705078125,1.05572509765625,1.05372314453125,1.05360107421875,1.05347900390625,1.05463867187500,1.05579833984375,1.05695800781250,1.05836181640625,1.05976562500000,1.06113281250000,1.06250000000000,1.06386718750000,1.06300048828125,1.06213378906250,1.05997314453125,1.05781250000000,1.05565185546875,1.05458984375000,1.05352783203125,1.05382080078125,1.05411376953125,1.05440673828125,1.05600585937500,1.05760498046875,1.05895996093750,1.06031494140625,1.06166992187500,1.06213378906250,1.06259765625000,1.06118164062500,1.05976562500000,1.05834960937500,1.05758056640625,1.05681152343750,1.05704345703125,1.05727539062500,1.05750732421875,1.05792236328125,1.05833740234375,1.05870361328125,1.05906982421875,1.05943603515625,1.05900878906250,1.05858154296875,1.05729980468750,1.05601806640625,1.05473632812500,1.05578613281250,1.05683593750000,1.05772705078125,1.05861816406250,1.05950927734375,1.05916748046875,1.05882568359375,1.05865478515625,1.05848388671875,1.05831298828125,1.05499267578125,1.05167236328125,1.05084228515625,1.05001220703125,1.04918212890625,1.05084228515625,1.05250244140625,1.05368652343750,1.05487060546875,1.05605468750000,1.05795898437500,1.05986328125000,1.06052246093750,1.06118164062500,1.06184082031250,1.06134033203125,1.06083984375000,1.06125488281250,1.06166992187500,1.06208496093750,1.06254882812500,1.06301269531250,1.06287841796875,1.06274414062500,1.06260986328125,1.05985107421875,1.05709228515625,1.05455322265625,1.05201416015625,1.04947509765625,1.04656982421875,1.04366455078125,1.04007568359375,1.03648681640625,1.03289794921875,1.02879638671875,1.02469482421875,1.01777343750000,1.01085205078125,1.00393066406250,0.996545410156250,0.989160156250000,0.983422851562500,0.977685546875000,0.971948242187500,0.969030761718750,0.966113281250000,0.964123535156250,0.962133789062500,0.960144042968750,0.955029296875000,0.949914550781250,0.941577148437500,0.933239746093750,0.924902343750000,0.916162109375000,0.907421875000000,0.902453613281250,0.897485351562500,0.892517089843750,0.892053222656250,0.891589355468750,0.893286132812500,0.894982910156250,0.896679687500000,0.899853515625000,0.903027343750000,0.902966308593750,0.902905273437500,0.902844238281250,0.899218750000000,0.895593261718750,0.892932128906250,0.890270996093750,0.887609863281250,0.882482910156250,0.877355957031250,0.872277832031250,0.867199707031250,0.862121582031250,0.856591796875000,0.851062011718750,0.840356445312500,0.829650878906250,0.818945312500000,0.801977539062500,0.785009765625000,0.764086914062500,0.743164062500000,0.722241210937500,0.706884765625000,0.691528320312500,0.685009765625000,0.678491210937500,0.671972656250000,0.668554687500000,0.665136718750000,0.657556152343750,0.649975585937500,0.642395019531250,0.624658203125000,0.606921386718750,0.579699707031250,0.552478027343750,0.525256347656250,0.503479003906250,0.481701660156250,0.478454589843750,0.475207519531250,0.471960449218750,0.475024414062500,0.478088378906250,0.485534667968750,0.492980957031250,0.500427246093750,0.513696289062500,0.526965332031250,0.546557617187500,0.566149902343750,0.585742187500000,0.586132812500000,0.586523437500000,0.549987792968750,0.513452148437500,0.476916503906250,0.436767578125000,0.396618652343750,0.349230957031250,0.301843261718750,0.254455566406250,0.235607910156250,0.216760253906250,0.426306152343750,0.635852050781250,0.845397949218750,1.37720947265625,1.90902099609375,2.38913574218750,2.86925048828125,3.34936523437500,3.50461425781250,3.65986328125000,3.59554443359375,3.53122558593750,3.46690673828125,3.09161376953125,2.71632080078125,2.55278320312500,2.38924560546875,2.22570800781250,2.27666015625000,2.32761230468750,2.34796142578125,2.36831054687500,2.38865966796875,2.39906005859375,2.40946044921875,2.30695800781250,2.20445556640625,2.10195312500000,2.08405761718750,2.06616210937500,2.15434570312500,2.24252929687500,2.33071289062500,2.37828369140625,2.42585449218750,2.36550292968750,2.30515136718750,2.24479980468750,2.13863525390625,2.03247070312500,1.85861816406250,1.68476562500000,1.51091308593750,1.34918212890625,1.18745117187500,1.06405029296875,0.940649414062500,0.817248535156250,0.798754882812500,0.780261230468750,0.769384765625000,0.758508300781250,0.747631835937500,0.779589843750000,0.811547851562500,0.856823730468750,0.902099609375000,0.947375488281250,0.975024414062500,1.00267333984375,1.02377929687500,1.04488525390625,1.06599121093750,1.06306152343750,1.06013183593750,1.07025146484375,1.08037109375000,1.09049072265625,1.08222656250000,1.07396240234375,1.07794189453125,1.08192138671875,1.08590087890625,1.08752441406250,1.08914794921875,1.08244628906250,1.07574462890625,1.06904296875000,1.06049804687500,1.05195312500000,1.04566650390625,1.03937988281250,1.03309326171875,1.02957763671875,1.02606201171875,1.02071533203125,1.01536865234375,1.01002197265625,1.01392822265625,1.01783447265625,1.02772216796875,1.03516845703125,1.04261474609375,1.05305175781250,1.06348876953125,1.07145996093750,1.08572998046875,1.10000000000000,1.09990234375000,1.09980468750000,1.09970703125000,1.09197998046875,1.08425292968750,1.06192626953125,1.03959960937500,1.01727294921875,1.00103759765625,0.984802246093750,0.979992675781250,0.975183105468750,0.970373535156250,0.985168457031250,0.999963378906250,1.02354736328125,1.04713134765625,1.07071533203125,1.07745361328125,1.08419189453125,1.07707519531250,1.06995849609375,1.06284179687500,1.02681884765625,0.990795898437500,0.973181152343750,0.955566406250000,0.937951660156250,0.940673828125000,0.943395996093750,0.962158203125000,0.980920410156250,0.999682617187500,1.04293212890625,1.08618164062500,1.11110839843750,1.13603515625000,1.16096191406250,1.15572509765625,1.15048828125000,1.12604980468750,1.10161132812500,1.07717285156250,1.05093994140625,1.02470703125000,1.01304931640625,1.00139160156250,0.989733886718750,0.991345214843750,0.992956542968750,1.00462646484375,1.01629638671875,1.02796630859375,1.03481445312500,1.04166259765625,1.04230957031250,1.04295654296875,1.04360351562500,1.04697265625000,1.05034179687500,1.05089111328125,1.05144042968750,1.05198974609375,1.05598144531250,1.05997314453125,1.05994873046875,1.05992431640625,1.05989990234375,1.05280761718750,1.04571533203125,1.03997802734375,1.03424072265625,1.02850341796875,1.01979980468750,1.01109619140625,1.01168212890625,1.01226806640625,1.01285400390625,1.02327880859375,1.03370361328125,1.04107666015625,1.04844970703125,1.05582275390625,1.06148681640625,1.06715087890625,1.06439208984375,1.06163330078125,1.05887451171875,1.05078125000000,1.04268798828125,1.03803710937500,1.03338623046875,1.02873535156250,1.02788085937500,1.02702636718750,1.02750244140625,1.02797851562500,1.02845458984375,1.03327636718750,1.03809814453125,1.03778076171875,1.03746337890625,1.03714599609375,1.03704833984375,1.03695068359375,1.03753662109375,1.03812255859375,1.03870849609375,1.03560791015625,1.03250732421875,1.03477783203125,1.03704833984375,1.03931884765625,1.04057617187500,1.04183349609375,1.04377441406250,1.04571533203125};
        int i;
        double[] data = new double[500];

        data = FilterMAF(falldata.rawdata2, 10);
        for (i = 0; i < 10; i++)
            data[i] = 1;

        double[] data_normalization = new double[500];
        data_normalization = DataNormalization(data, 30);
        double[] output = new double[500];

        output = FallDetection(data_normalization, transitionMatrix, emissionMatrix, startMatrix);

        for (i = 0; i < 500; i++) {

            Log.w(TAG, "output_result" + "=" + output[i]*100 + "   " +i);
            counttt++;
            //System.out.println(output[i]);
        }



    }
        static double []FallDetection(double[] input,double[][] transitionProbability/*33*/,double[][] emissionProbability/*35*/,double[] startProbability)
        {

            double maxdata=input[0],mindata=input[0];
            int maxIndex=0,minIndex=0,i,j;

            //取输入序列最大值、最小值，以及最大值和最小值的位置
            for(i=0;i<input.length;i++){
                if(input[i]>maxdata){
                    maxdata=input[i];
                    maxIndex=i;
                }
                if(input[i]<mindata){
                    mindata=input[i];
                    minIndex=i;
                }
            }

            double unit = (maxdata - mindata) / 5;
            // int distance = maxIndex - minIndex + 1;
            double []data_temp=new double[50],marching_degree=new double[500];
            double max_value,min_value;
            //int []ats=new int[10];
            double []ats=new double[10];

            for(i=0;i<500;i++){
                marching_degree[i]=0;
            }
            for (i=0;i<451;i++){

                max_value=input[i];
                min_value=input[i];
                for(j=i;j<i+50;j++){
                    data_temp[j-i]=input[j];
                    if(input[j]>max_value){
                        max_value=input[j];
                    }
                    if(input[j]<min_value){
                        min_value=input[j];
                    }
                }
                if(max_value > mindata + unit * 2 && min_value < mindata + unit){
                    ats=AccCharacteristicSeriesExtraction(data_temp, 10, 5, mindata, unit);
                   // marching_degree[i+49] = HMM_BaumWelch(transitionProbability, emissionProbability, startProbability, ats);
                    marching_degree[i+49] = probsComputation(ats, ats.length);
                   // marching_degree[i+49] = probsComputation1(ats, ats.length);


                   //
                   // marching_degree[i+49] = likelihoodComputation(ats,10);
                }
                else{
                    marching_degree[i+49] = 0;
                }

            }


            return marching_degree;
        }


//    static double HMM_BaumWelch(double[][] transitionProbability/*33*/, double[][] emissionProbability/*35*/, double[] startProbability, int[] observation) {
//
//        int i, j;
//        int oSeriesLength = observation.length;
//
//        double[][] states = new double[9][5];
//
//        for (i = 0; i < emissionProbability.length; i++)
//            for (j = 0; j < emissionProbability[i].length; j++) {
//                states[3 * i][j] = transitionProbability[0][i] * emissionProbability[i][j];
//                states[3 * i + 1][j] = transitionProbability[1][i] * emissionProbability[i][j];
//                states[3 * i + 2][j] = transitionProbability[2][i] * emissionProbability[i][j];
//            }
//        double temp1, temp2, temp3;
//        if (observation[0] == 1) {
//            temp1 = startProbability[0] * emissionProbability[0][0];
//            temp2 = startProbability[1] * emissionProbability[1][0];
//            temp3 = startProbability[2] * emissionProbability[2][0];
//        } else if (observation[0] == 2) {
//            temp1 = startProbability[0] * emissionProbability[0][1];
//            temp2 = startProbability[1] * emissionProbability[1][1];
//            temp3 = startProbability[2] * emissionProbability[2][1];
//        } else if (observation[0] == 3) {
//            temp1 = startProbability[0] * emissionProbability[0][2];
//            temp2 = startProbability[1] * emissionProbability[1][2];
//            temp3 = startProbability[2] * emissionProbability[2][2];
//        } else if (observation[0] == 4) {
//            temp1 = startProbability[0] * emissionProbability[0][3];
//            temp2 = startProbability[1] * emissionProbability[1][3];
//            temp3 = startProbability[2] * emissionProbability[2][3];
//        } else {//o[0]==5
//            temp1 = startProbability[0] * emissionProbability[0][4];
//            temp2 = startProbability[1] * emissionProbability[1][4];
//            temp3 = startProbability[2] * emissionProbability[2][4];
//        }
//        double[][] a = new double[observation.length][3];
//        double[] forwardPro = new double[observation.length];
//        a[0][0] = temp1;
//        a[0][1] = temp2;
//        a[0][2] = temp3;
//        forwardPro[0] = temp1 + temp2 + temp3;
//        for (i = 1; i < observation.length; i++) {
//            temp1 = a[i - 1][0] * states[0][observation[i] - 1] + a[i - 1][1] * states[1][observation[i] - 1] + a[i - 1][2] * states[2][observation[i] - 1];
//            temp2 = a[i - 1][0] * states[3][observation[i] - 1] + a[i - 1][1] * states[4][observation[i] - 1] + a[i - 1][1] * states[5][observation[i] - 1];
//            temp3 = a[i - 1][0] * states[6][observation[i] - 1] + a[i - 1][1] * states[7][observation[i] - 1] + a[i - 1][1] * states[8][observation[i] - 1];
//            a[i][0] = temp1;
//            a[i][1] = temp2;
//            a[i][2] = temp3;
//            forwardPro[i] = temp1 + temp2 + temp3;
//        }
//
//       // double hmm_temp = forwardPro[oSeriesLength - 1] * 1000;
//        double hmm_temp = temp3 / forwardPro[oSeriesLength - 1] * 100;
//
//        if (hmm_temp < 0.1)
//            hmm_temp = 0;
//        return hmm_temp;
//    }

    public static double probsComputation(double[] observationSequences, int L){

        double[][] forwardProbs = new double[M][L];
        double conditionalProbs = 0;

        for (int i = 0; i < N; i++){
            if (observationSequences[0] == i){
                for (int j = 0; j < M; j++){
                    forwardProbs[j][0] = DoubleUtil.mul(startMatrix[j], emissionMatrix[j][i]);
                }
            }
        }

        double sec_temp = 0;
        for (int i = 1; i < L; i++){
            for (int j = 0; j < M; j++){
                for (int k = 0; k < N; k++){
                    if (observationSequences[i] == k){
                        for (int jj = 0; jj < M; jj++){
                            sec_temp = DoubleUtil.add(sec_temp, DoubleUtil.mul(forwardProbs[jj][i - 1], DoubleUtil.mul(transitionMatrix[jj][j], emissionMatrix[j][k])));
                        }
                        forwardProbs[j][i] = sec_temp;
                        sec_temp = 0;
                    }
                }
            }
        }

        double thr_temp = 0;
        for (int i = 0; i < M; i++){
            thr_temp = DoubleUtil.add(thr_temp, forwardProbs[i][L - 1]);
        }

        conditionalProbs = DoubleUtil.divide(forwardProbs[M - 1][L - 1], thr_temp, 4);

        return conditionalProbs;
    }








    public static double likelihoodComputation(double[] observationSequences, int length){
        double[] o = observationSequences;
        int l = length;
        double[][] forwardProbability = new double[M][l];
        double probability;

		/*forward algorithm - 1st step*/
        for (int i = 0; i < M; i++){
            for (int j = 0; j < N; j++){
                if (o[0] == j){
                    forwardProbability[i][0] = startMatrix[i] * emissionMatrix[i][j];
                }
            }
        }

		/*forward algorithm - 2ed step*/
        double sum = 0;
        for (int i = 1; i < l; i++){
            for(int j = 0; j < M; j++){
                for (int k = 0; k < N; k++){
                    if (o[i] == k){
                        for (int ii = 0; ii < M; ii++){
                            sum = sum + forwardProbability[ii][i - 1] * transitionMatrix[ii][j] * emissionMatrix[j][k];
                        }
                        forwardProbability[j][i] = sum;
                        sum = 0;
                    }
                }
            }
        }

		/*forward algorithm - 3rd step*/
        double temp = 0;
        for (int i = 0; i < M; i++){
            temp = temp + forwardProbability[i][l - 1];
        }
        probability = forwardProbability[M - 1][l - 1] / temp;

        return probability;
    }

    public static double probsComputation1(double[] observationSequences, int L){

        double[][] forwardProbs = new double[M][L];
        double conditionalProbs = 0;

        for (int i = 0; i < N; i++){
            if (observationSequences[0] == i){
                for (int j = 0; j < M; j++){
                    // forwardProbs[j][0] = DoubleUtil.mul(startMatrix[j][0], emissionMatrix[j][i]);
                    forwardProbs[j][0] = startMatrix[j] * emissionMatrix[j][i];
                }
            }
        }

        double sec_temp = 0;
        for (int i = 1; i < L; i++){
            for (int j = 0; j < M; j++){
                for (int k = 0; k < N; k++){
                    if (observationSequences[i] == k){
                        for (int jj = 0; jj < M; jj++){
                            // sec_temp = DoubleUtil.add(sec_temp, DoubleUtil.mul(forwardProbs[jj][i - 1], DoubleUtil.mul(transitionMatrix[jj][j], emissionMatrix[j][k])));
                            sec_temp = sec_temp + forwardProbs[jj][i - 1] * transitionMatrix[jj][j] * emissionMatrix[j][k];
                        }
                        forwardProbs[j][i] = sec_temp;
                        sec_temp = 0;
                    }
                }
            }
        }

        double thr_temp = 0;
        for (int i = 0; i < M; i++){
            // thr_temp = DoubleUtil.add(thr_temp, forwardProbs[i][L - 1]);
            thr_temp = thr_temp + forwardProbs[i][L - 1];
        }

        // conditionalProbs = DoubleUtil.divide(forwardProbs[M - 1][L - 1], thr_temp, 4);
        conditionalProbs = forwardProbs[M - 1][L - 1] / thr_temp;

        return conditionalProbs;
    }





    static double[] AccCharacteristicSeriesExtraction(double[] input, int elements_num, int elements_length, double minInput, double unit) {
        int i, j;
        double[][] firinput = new double[10][5];
        double[] secinput = new double[10];
        for (i = 0; i < elements_num; i++) {
            for (j = 0; j < elements_length; j++) {
                firinput[i][j] = input[i * elements_length + j];
            }
            secinput[i] = 0;
            for (j = 0; j < firinput[i].length; j++) {
                secinput[i] += firinput[i][j];
            }
            secinput[i] /= firinput[i].length;
        }
        double[] thrinput = new double[10];
        //已改范围1-5为0-4
        for (i = 0; i < elements_num; i++) {
            if (secinput[i] >= minInput && secinput[i] < minInput + unit)
                thrinput[i] = 0;
            else if (secinput[i] >= minInput + unit && secinput[i] < minInput + unit * 2)
                thrinput[i] = 1;
            else if (secinput[i] >= minInput + unit * 2 && secinput[i] < minInput + unit * 3)
                thrinput[i] = 2;
            else if (secinput[i] >= minInput + unit * 3 && secinput[i] < minInput + unit * 4)
                thrinput[i] = 3;
            else if (secinput[i] >= minInput + unit * 4)
                thrinput[i] = 4;
        }
        return thrinput;
    }

    static double[] DataNormalization(double[] input, int fixed_length) {
        int i, j;
        int length = input.length;
        double maxValue = input[0], minValue = input[0];
        int maxIndex = 0, minIndex = 0;

        for (i = 0; i < input.length; i++) {
            if (input[i] > maxValue) {
                maxValue = input[i];
                maxIndex = i;
            }
            if (input[i] < minValue) {
                minValue = input[i];
                minIndex = i;
            }
        }

        int distance = maxIndex - minIndex + 1;

        if (minIndex >= maxIndex)
            return input;
        else if (distance >= fixed_length)
            return input;
        else {
            int difference = fixed_length - distance;
            double[] temp = new double[difference];
            for (i = 0; i < difference; i++) {
                temp[i] = minValue + (i + 1) * (maxValue - minValue) / (difference + 1);
            }


            double[] data_middle_part = new double[maxIndex - minIndex + 1 + difference];

            for (i = minIndex; i <= maxIndex; i++) {
                data_middle_part[i - minIndex] = input[i];
            }
            for (i = maxIndex - minIndex + 1; i < maxIndex - minIndex + 1 + difference; i++) {
                data_middle_part[i] = temp[i - maxIndex + minIndex - 1];
            }

            double tmp;
            for (i = 0; i < data_middle_part.length; i++) {
                for (j = i + 1; j < data_middle_part.length; j++) {
                    if (data_middle_part[j] < data_middle_part[i]) {
                        tmp = data_middle_part[j];
                        data_middle_part[j] = data_middle_part[i];
                        data_middle_part[i] = tmp;
                    }
                }
            }


            double[] data_final = new double[500];
            for (i = 0; i < minIndex; i++) {
                data_final[i] = input[i];
            }
            for (i = 0; i < data_middle_part.length; i++) {
                data_final[i + minIndex] = data_middle_part[i];
            }
            for (i = maxIndex + 1; i < input.length - difference; i++) {
                data_final[i + minIndex + data_middle_part.length - maxIndex - 1] = input[i];
            }

            return data_final;
        }
    }


    static double[] FilterMAF(double[] Data, int Order) {
        int k, num, len = Data.length;
        double sum;
        double[] filter = new double[500], MAF_data = new double[10];
        for (k = 0; k < Order; k++) {
            MAF_data[k] = 0;
        }

        for (k = 0; k < len; k++)
            filter[k] = 0;
        for (num = 0; num < len; num++) {
            for (k = 9; k > 0; k--)
                MAF_data[k] = MAF_data[k - 1];
            MAF_data[0] = Data[num];
            sum = 0;
            for (k = 0; k < Order; k++) {
                sum = sum + MAF_data[k];
            }
            filter[num] = sum / Order;
        }

        return filter;
    }





}
