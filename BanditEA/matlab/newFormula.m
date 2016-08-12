total=[];
%R=[1 2 3 4 5 10 50 100 100];
D=[10 50 100 250 500 750 1000];
R=[1:100];

MIN=zeros(2,length(D));

for dId=1:length(D)
n=D(dId);
totalEvals=[];
for r=R
p=1/2+erf(sqrt(r)/2)/2;
%p=erf(sqrt(r));
E=zeros(1,n);
E(1)=1/p;
for i=1:n-1
E(i+1) = n/(n-i)/p + i/(n-i)/p*(1-p)*E(i);
% if(E(i+1)>1000000)
%     break
% end
end
if(sum(E)*2*r > 10^10)
    %totalEvals=[totalEvals NaN];
    totalEvals=[totalEvals sum(E)*2*r];
else
    totalEvals=[totalEvals sum(E)*2*r];
end
end
[MIN(2,dId),MIN(1,dId)]=min(totalEvals);
total=[total; totalEvals];
end
MIN
figure

mycolors={'k--','m-','y-','b-','r-','g-','k-', 'c-', 'm--', 'y--', 'b--'};
for id=1:length(D)
    plot(R,log10(total(id,:)),mycolors{id},'Linewidth',2);
    %plot(R,log10(total(id,:)/D(id)/(log(D(id)))^2),mycolors{id},'Linewidth',2);
    hold on
end
plot(R(MIN(1,:)),log10(MIN(2,:)),'o','Linewidth',2, 'Color', [0.5 0.5 0.5]);
xlabel('Resampling number','FontSize',16);
ylabel(sprintf('%s','log_{10}(Expectation of #Evaluations)'),'FontSize',16);
set(gca,'FontSize',16)
h=legend('n=10', 'n=50','n=100','n=250','n=500','n=750','n=1000', 'Optimal r');
%h=legend('n=10', 'n=50','n=100','n=250','n=500','n=750','n=1000');
title('Exact computation time for RMHC on noisy OneMax problem','FontSize',14);
set(h,'FontSize',14);
legend boxoff 
grid on
%axis([0 100 1 10]);

%figure
%plot(D,R(MIN(1,:)))