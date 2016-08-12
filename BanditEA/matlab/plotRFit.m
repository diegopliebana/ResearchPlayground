mycolors={'k--o','mo-','y-o','b-o','r-o','g-o','k-o'};
R=[1 2 3 4 5 10];
idD=[1:20];

names={'results1plus1','resultsBandit'};
titles={'RMHC - OneMax problem','Bandit-based RMHC - OneMax problem'};
for test=1:2
    RES=zeros(length(idD),length(R));
    ERR=zeros(length(idD),length(R));
    idR=1;
    for r=R
        M=load(sprintf('%sNoise1_%d.txt',names{test},r));
        RES(1:length(M(:,2)),idR)=M(:,2);
        for i=length(M(:,2))+1:length(idD)
            RES(i,idR)=NaN;
            ERR(i,idR)=NaN;
        end
        idR=idR+1;
    end
    figure(test)
    hold on    
    idC=1;
    for d=[1 2 3 6 11 16 21]
        %errorbar(R,log(RES(d,:)),ERR(d,:) ,mycolors{idC},'Linewidth',2)
        plot(R,log(RES(d,:))/log(10),mycolors{idC},'Linewidth',2);
        idC=idC+1;
    end
    xlabel('Resampling number','FontSize',16);
    ylabel(sprintf('%s','log_{10}(Evaluations)'),'FontSize',16);
    set(gca,'FontSize',16)
    h=legend('n=10', 'n=50','n=100','n=250','n=500','n=750','n=1000');
    title(titles{test},'FontSize',14);
    set(h,'FontSize',14);
    legend boxoff 
    grid on
    axis([0 10 1 6]);
end
legend boxoff 