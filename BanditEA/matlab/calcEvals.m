N=[10 50 100 250 500 750 1000];
R= [1 2 3 4 5 10];
mycolors={'k--o','mo-','y-o','b-o','r-o','g-o','k-o'};

figure
hold on

id=1;
for n=N
    meanE=[];
    stdE=[];
    for r=R
      evals=rmhcEvalsP(n,r);
      if(mean(evals)>=1000000)
          meanE=[meanE NaN];
      else
          meanE=[meanE log10(mean(evals))];
      end
      stdE=[stdE std(log10(evals))/sqrt(length(evals))];
    end
    errorbar(R,meanE,stdE,mycolors{id},'Linewidth',2);
    %plot(R,log(E));
    id=id+1;
end
xlabel('Resampling number','FontSize',16);
ylabel(sprintf('%s','log_{10}(Evaluations)'),'FontSize',16);
set(gca,'FontSize',16)
h=legend('n=10', 'n=50','n=100','n=250','n=500','n=750','n=1000');
title('RMHC - OneMax problem','FontSize',14);
set(h,'FontSize',14);
legend boxoff 
grid on
axis([0 10 1 6]);