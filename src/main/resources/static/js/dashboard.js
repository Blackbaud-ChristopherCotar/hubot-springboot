$('document').ready(function(){
    var Metrics = React.createClass({
        loadMetrics: function() {
            $.ajax({
                url: 'http://localhost:8001/metrics',
                dataType: 'json',
                success: function(data) {
                    this.setState({data: JSON.stringify(data)});
                    console.log(JSON.stringify(data));
                }.bind(this),
                error: function(xhr, status, err) {
                    console.error(this.props.url, status, err.toString());
                }.bind(this)
            });
        },
        getInitialState: function() {
            return {data: {}};
        },
        componentDidMount: function() {
            this.loadMetrics();
        },
        render: function() {
            return React.createElement("div", null, this.state.data);
        }
    });

    React.render(
       React.createElement(Metrics, null),
       document.getElementById('container')
    );
});