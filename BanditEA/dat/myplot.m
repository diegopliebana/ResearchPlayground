% plot fitness
figure
plot(log([1:n]), ones(1,n)*161, 'k--');
hold on

M1=load('dat/simpleEA.txt');
[m,n]=size(M1);
[myMean1, myStd1]=myStd(M1);
errorbar(log([1:n]), myMean1, myStd1, 'k');


M2=load('dat/banditEA_C1.txt');
[m,n]=size(M2);
[myMean2, myStd2]=myStd(M2);
errorbar(log([1:n]), myMean2, myStd2, 'b');

M2=load('dat/banditEA_C10000.txt');
[m,n]=size(M2);
[myMean2, myStd2]=myStd(M2);
errorbar(log([1:n]), myMean2, myStd2, 'g');

M3=load('dat/mBanditEA_C1.txt');
[m,n]=size(M3);
[myMean3, myStd3]=myStd(M3);
errorbar(log([1:n]), myMean3, myStd3, 'r');

M3=load('dat/mBanditEA_C10.txt');
[m,n]=size(M3);
[myMean3, myStd3]=myStd(M3);
errorbar(log([1:n]), myMean3, myStd3, 'm');

title('MaxSat');
ylabel('Number of false clauses','FontSize',16);
xlabel('log(Evaluations)','FontSize',16);
set(gca,'FontSize',16)


legend('Optimum=161', 'SimpleEA', 'BanditEA C=1', 'BanditEA C=10,000', 'mBanditEA C=1', 'mBanditEA C=10');