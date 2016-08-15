% test Equation 20
R=[1:50];
figure
for n=[2:100]
f=[]
for r=R
f=[f 2*r*n/(1/2-erf(sqrt(r)/2)/2)/((1/2+erf(sqrt(r)/2)/2)^n)];
end
plot(R,log10(f))
hold on
end
xlabel('Resampling number','FontSize',16);
ylabel(sprintf('%s','log_{10}(Evaluations)'),'FontSize',16);
set(gca,'FontSize',16)
%h=legend('n=10', 'n=50','n=100','n=250','n=500','n=750','n=1000');
title('RMHC - Noisy OneMax','FontSize',14);
set(h,'FontSize',14);
legend boxoff 
grid on