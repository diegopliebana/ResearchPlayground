% test RMHCNoisyTrap(C,n,sigma,r)
N=[20:25];% 15 20];% 25 30 35 40 45 50]% 100 250 500 750 1000];

sigma=1;
R=[1,2,3,4,5,10,50,100];
mycolors={'k--','m-','y-','b-','r-','g-','k-', 'c-', 'm--', 'y--', 'b--'};

figure(10)
hold on
id=1;
for n=N
E=[];
C=n+1;
for r=R
    evals=[];
    for test=1:100
        res=RMHCNoisyTrap(C,n,sigma,r);
        evals=[evals res(2)];
    end
    E=[E log10(mean(evals))];
end
%figure(10)
plot(R,E,mycolors{id},'Linewidth',2);
id=id+1;
end
xlabel('Resampling number','FontSize',16);
ylabel(sprintf('%s','log_{10}(Evaluations)'),'FontSize',16);
set(gca,'FontSize',16)
h=legend('n=10', 'n=50','n=100','n=250','n=500','n=750','n=1000');
title('RMHC - Trap function','FontSize',14);
set(h,'FontSize',14);
legend boxoff 
grid on
%axis([0 100 0 60000000]);