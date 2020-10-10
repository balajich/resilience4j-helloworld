# vi: set ft=ruby :

ENV['VAGRANT_NO_PARALLEL'] = 'yes'

Vagrant.configure(2) do |config|
  
  config.vm.define "r4jserver" do |r4jserver|
    r4jserver.vm.box = "centos/7"
    r4jserver.vm.hostname = "r4jserver.eduami.org"
    r4jserver.vm.network "private_network", ip: "192.168.50.26"
    r4jserver.vm.network "forwarded_port", guest: 8080, host: 8080
    r4jserver.vm.provision "shell", path: "startup-r4jserver.sh"
    r4jserver.vm.provider "virtualbox" do |vb|
      vb.name = "r4jserver"
      vb.memory = 4024
      vb.cpus = 1
    end
  end
end
