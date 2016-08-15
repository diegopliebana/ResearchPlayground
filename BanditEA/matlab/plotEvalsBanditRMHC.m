N=[10 50 100 250 500 750 1000];
R= [1 2 3 4 5 10];
mycolors={'k--o','mo-','y-o','b-o','r-o','g-o','k-o'};

figure
hold on

id=1;
ave=load('../newResults/ave_banditRMHC_nohist.dat');
se=load('../newResults/se_banditRMHC_nohist.dat');

for n=N
    meanE=ave((id-1)*6+1:id*6);
    stdE=se((id-1)*6+1:id*6);
    meanE'
    plot(R,log10(meanE),mycolors{id},'Linewidth',2);
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
%axis([0 10 1 6]);