function res=NoisyTrap(C,X,sigma,r)
res=Trap(C,X)+randn*sigma/sqrt(r);
end