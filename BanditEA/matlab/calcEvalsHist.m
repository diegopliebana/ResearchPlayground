N=[10 50 100 250 500 750 1000];
%R= [1 2 3 4 5 10 20 30 40 50 60 70 80 90 100];
R1=[1:20];
R2=[25:5:100];
R=[R1 R2];
mycolors={'k--o','mo-','y-o','b-o','r-o','g-o','k-o'};

figure
hold on

id=1;
M=[];
stdM=[];
for n=N
    meanE=[];
    stdE=[];
    for r=R
      evals=rmhcEvalsHist(n,r);
      meanTmp = mean(log10(evals));
      if(meanTmp>=7)
          meanE=[meanE NaN];
      else
          meanE=[meanE meanTmp];
      end
      stdE=[stdE std(log10(evals))/sqrt(length(evals))];
    end
    M=[M;meanE];
    stdM=[stdM;stdE];
    errorbar(R,meanE,stdE,mycolors{id},'Linewidth',2);
    %plot(R,log(E));
    id=id+1;
end
xlabel('Resampling number','FontSize',16);
ylabel(sprintf('%s','log_{10}(Evaluations)'),'FontSize',16);
set(gca,'FontSize',16)
h=legend('n=10', 'n=50','n=100','n=250','n=500','n=750','n=1000');
%title('RMHC - OneMax problem','FontSize',14);
title('RMHC on noisy OneMax problem with resampling, no statistic stored','FontSize',14);

set(h,'FontSize',14);
legend boxoff 
grid on
axis([0 100 1 7]);
M