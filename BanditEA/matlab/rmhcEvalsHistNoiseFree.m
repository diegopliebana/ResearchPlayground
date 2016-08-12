function E=rmhcEvalsHistNoiseFree(n)
E=[];
for test=1:100
    evals=0;
    i=0;
    while(i<n && evals<10000000)
        p=rand;
        if(p>=i/n)
            i=i+1;
        end
        evals=evals+1;
    end
    E=[E evals];
end
end