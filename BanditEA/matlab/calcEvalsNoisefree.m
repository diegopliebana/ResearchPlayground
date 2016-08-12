N=[10 50 100 250 500 750 1000];
R= [1 2 3 4 5 10 50 100];
mycolors={'k--o','mo-','y-o','b-o','r-o','g-o','k-o'};

figure
hold on

id=1;
M=[];
stdE=[];
for n=N
    evals=rmhcEvalsHistNoiseFree(n);
    %errorbar(R,ones(1,8)*mean(log10(evals)),ones(1,8)*std(log10(evals))/10, mycolors{id},'Linewidth',2);
    id=id+1;
    M=[M, mean(log10(evals))];
    stdE=[stdE, std(log10(evals))/10];
end
figure
errorbar(N, M, stdE, 'Linewidth',2)
xlabel('Problem dimension','FontSize',16);
ylabel(sprintf('%s','log_{10}(Evaluations)'),'FontSize',16);
set(gca,'FontSize',16)
h=legend('Empirical evaluation number');
title('RMHC - Noise-free OneMax problem','FontSize',14);
set(h,'FontSize',14);
legend boxoff 
grid on
%axis([0 10 1 6]);
